package com.snailwu.job.admin.core.thread;

import com.snailwu.job.admin.core.conf.AdminConfig;
import com.snailwu.job.admin.core.cron.CronExpression;
import com.snailwu.job.admin.core.model.JobInfo;
import com.snailwu.job.admin.trigger.TriggerTypeEnum;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.mybatis.dynamic.sql.render.RenderingStrategies;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import static com.snailwu.job.admin.mapper.JobInfoDynamicSqlSupport.*;
import static org.mybatis.dynamic.sql.SqlBuilder.*;

/**
 * 定时任务调度类
 * 扫描数据库里的定时任务，将马上要进行调度的任务加入到 key 为 秒，val 为任务集合的 Map 中
 * 根据秒获取对应的任务集合，进行任务的调度
 *
 * @author 吴庆龙
 * @date 2020/7/9 2:40 下午
 */
public class JobScheduleHelper {
    private static final Logger LOGGER = LoggerFactory.getLogger(JobScheduleHelper.class);

    /**
     * 扫描可调度的任务线程
     */
    private static Thread scanJobThread;
    private static volatile boolean scanJobStopFlag = false;

    /**
     * 进行调度线程
     */
    private static Thread invokeJobThread;
    private static volatile boolean invokeJobStopFlag = false;

    /**
     * 提前 5 秒加载数据
     */
    private static final long PRE_READ_MS = 5000;

    /**
     * 缓存将要执行的任务
     * key: 秒
     * val: 任务ID集合
     */
    private static final Map<Integer, Set<Integer>> INVOKE_JOB_MAP = new ConcurrentHashMap<>();

    /**
     * 每次获取任务的最大数量
     */
    private static final int MAX_LIMIT_PRE_READ = 100;

    /**
     * 启动线程
     */
    public static void start() {
        scanJobThread = new Thread(() -> {
            // 对齐整秒
            try {
                TimeUnit.MILLISECONDS.sleep(1000 - System.currentTimeMillis() % 1000);
            } catch (InterruptedException e) {
                LOGGER.error("[SnailJob]-对齐整秒休眠异常.原因:{}", e.getMessage());
            }
            while (!scanJobStopFlag) {
                // 当前时间戳, 到这里后是"17:06:25,003"比整秒大一点
                long nowTimeTs = System.currentTimeMillis();

                // 获取 触发时间小于等于该时间戳的所有任务
                long maxTriggerNextTime = nowTimeTs + PRE_READ_MS;

                // 扫描将要待执行的任务
                List<JobInfo> jobInfoList = AdminConfig.getInstance().getJobInfoMapper().selectMany(
                        select(jobInfo.allColumns())
                                .from(jobInfo)
                                .where(triggerStatus, isEqualTo((byte) 1)) // 可运行的
                                .and(triggerNextTime, isLessThanOrEqualTo(maxTriggerNextTime)) // 下次调度时间小于等于未来5秒
                                .limit(MAX_LIMIT_PRE_READ)
                                .build().render(RenderingStrategies.MYBATIS3)
                );

                // 遍历待执行的任务
                for (JobInfo info : jobInfoList) {
                    // 1. 过时的任务 - 忽略
                    // 防止出现触发时间是"17:06:25,000"，当前时间是"17:06:25,003"的情况
                    if (info.getTriggerNextTime() < nowTimeTs - 999) {
                        LOGGER.warn("[SnailJob]-任务:{},错失触发时间:{}", info.getId(),
                                DateFormatUtils.format(info.getTriggerNextTime(), "yyyy-MM-dd HH:mm:ss,SSS"));

                        // 计算任务从当前时间开始的下次执行时间
                        refreshNextValidTime(info, new Date());
                        continue;
                    }

                    // 2. 剩下的任务就是接下来 5 秒内要执行的任务
                    while (info.getTriggerNextTime() <= maxTriggerNextTime && info.getTriggerNextTime() != 0L) {
                        // 当前任务需要在 X 秒执行
                        int invokeSecond = (int) ((info.getTriggerNextTime() / 1000) % 60);
                        LOGGER.info("[SnailJob]-任务:{},执行时间:{}", info.getId(),
                                DateFormatUtils.format(info.getTriggerNextTime(), "yyyy-MM-dd HH:mm:ss,SSS"));

                        // 放入执行队列
                        pushInvokeMap(invokeSecond, info.getId());

                        // 刷新任务的下次执行时间
                        refreshNextValidTime(info, new Date());
                    }
                }

                // 更新任务的下次执行时间
                for (JobInfo info : jobInfoList) {
                    AdminConfig.getInstance().getJobInfoMapper().update(
                            update(jobInfo)
                                    .set(triggerLastTime).equalTo(info.getTriggerLastTime())
                                    .set(triggerNextTime).equalTo(info.getTriggerNextTime())
                                    .set(triggerStatus).equalTo(info.getTriggerStatus())
                                    .where(id, isEqualTo(info.getId()))
                                    .build().render(RenderingStrategies.MYBATIS3)
                    );
                }
                long costTs = System.currentTimeMillis() - nowTimeTs;
                LOGGER.info("[SnailJob]-本次任务扫描整理耗时:{}毫秒", costTs);

                // 耗时大于预读时间，不进行休眠
                if (costTs > PRE_READ_MS) {
                    // 几乎不可能运行到这，如果运行到这则会丢失一些任务调度
                    LOGGER.warn("[SnailJob]-本次任务扫描整理耗时过长,可能会丢失一些任务调度.");
                    continue;
                }

                // 休眠
                try {
                    TimeUnit.MILLISECONDS.sleep(PRE_READ_MS - System.currentTimeMillis() % 1000);
                } catch (InterruptedException e) {
                    if (!scanJobStopFlag) {
                        LOGGER.error("对齐整秒，休眠异常", e);
                    }
                }
            }
        });
        scanJobThread.setDaemon(true);
        scanJobThread.setName("ScheduleThread");
        scanJobThread.start();

        // 执行调度任务线程
        invokeJobThread = new Thread(() -> {
            // 对齐整秒
            try {
                TimeUnit.MILLISECONDS.sleep(1000 - System.currentTimeMillis() % 1000);
            } catch (InterruptedException e) {
                LOGGER.error("对齐整秒，休眠异常", e);
            }

            while (!invokeJobStopFlag) {
                // 当前的秒
                int nowSecond = Calendar.getInstance().get(Calendar.SECOND);

                // 获取本秒以及前一秒的所有 jobId
                List<Integer> jobIdList = new ArrayList<>();
                for (int i = 0; i < 2; i++) {
                    Set<Integer> list = INVOKE_JOB_MAP.remove((nowSecond + 60 - i) % 60);
                    if (list != null && !list.isEmpty()) {
                        jobIdList.addAll(list);
                    }
                }

                // 进行调度
                for (Integer jobId : jobIdList) {
                    // 不指定 executorParam 使用 JobInfo 中的， failRetryCount 同理
                    JobTriggerPoolHelper.push(jobId, TriggerTypeEnum.CRON, -1, null);
                }
                jobIdList.clear();

                // 对齐整秒
                try {
                    TimeUnit.MILLISECONDS.sleep(1000 - System.currentTimeMillis() % 1000);
                } catch (InterruptedException e) {
                    if (!invokeJobStopFlag) {
                        LOGGER.error("对齐整秒，休眠异常", e);
                    }
                }
            }
        });
        invokeJobThread.setDaemon(true);
        invokeJobThread.setName("RingThread");
        invokeJobThread.start();
    }

    /**
     * 计算任务下次的执行时间
     */
    private static void refreshNextValidTime(JobInfo info, Date fromDate) {
        Date nextValidDate = null;
        try {
            nextValidDate = new CronExpression(info.getCron()).getNextValidTimeAfter(fromDate);
        } catch (ParseException e) {
            LOGGER.error("[SnailJob]-Cron表达式异常.jobId:{}", info.getId());
        }
        if (nextValidDate == null) {
            info.setTriggerLastTime(0L);
            info.setTriggerNextTime(0L);
            info.setTriggerStatus((byte) 0);
        } else {
            info.setTriggerLastTime(info.getTriggerNextTime());
            info.setTriggerNextTime(nextValidDate.getTime());
            info.setTriggerStatus((byte) 1);
        }
    }

    /**
     * 加入到执行队列中
     */
    private static void pushInvokeMap(int invokeSecond, int jobId) {
        Set<Integer> jobIdSet = INVOKE_JOB_MAP.computeIfAbsent(invokeSecond, k -> new HashSet<>());
        jobIdSet.add(jobId);
    }

    public static void stop() {
        // 设置为停止标志
        scanJobStopFlag = true;
        // 休眠一秒，如果线程主动执行完毕，则正常停止
        try {
            TimeUnit.MILLISECONDS.sleep(1000 - System.currentTimeMillis() % 1000);
        } catch (InterruptedException e) {
            LOGGER.error(e.getMessage(), e);
        }
        if (scanJobThread.getState() != Thread.State.TERMINATED) {
            scanJobThread.interrupt();
            try {
                scanJobThread.join();
            } catch (InterruptedException e) {
                LOGGER.error(e.getMessage(), e);
            }
        }

        // 调度队列中是否有未进行调度的数据
        if (!INVOKE_JOB_MAP.isEmpty()) {
            try {
                TimeUnit.SECONDS.sleep(8);
            } catch (InterruptedException e) {
                LOGGER.error(e.getMessage(), e);
            }
        }

        invokeJobStopFlag = true;
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            LOGGER.error(e.getMessage(), e);
        }
        if (invokeJobThread.getState() != Thread.State.TERMINATED) {
            invokeJobThread.interrupt();
            try {
                invokeJobThread.join();
            } catch (InterruptedException e) {
                LOGGER.error(e.getMessage(), e);
            }
        }

        LOGGER.info("JobScheduleHelper Stop.");
    }
}
