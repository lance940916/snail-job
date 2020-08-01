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

import static com.snailwu.job.admin.constant.JobConstants.DATE_TIME_PATTERN;
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
            // 加入接下来5秒内待调度的任务，也就是5秒后必须进行下一次任务的扫描
            final long preReadMs = 5000;

            while (!scanJobStopFlag) {
                long startTs = System.currentTimeMillis();

                // 当前整秒时间戳
                long nowTimeTs = startTs / 1000 * 1000;

                // 获取 触发时间小于等于该时间戳的所有任务
                long nextScanTs = nowTimeTs + preReadMs;

                // 扫描将要待执行的任务 FIXME 与数据库打交道,耗时未知,需要加缓存
                List<JobInfo> jobInfoList = AdminConfig.getInstance().getJobInfoMapper().selectMany(
                        select(jobInfo.id, jobInfo.triggerNextTime)
                                .from(jobInfo)
                                .where(triggerStatus, isEqualTo((byte) 1)) // 可运行的
                                .and(triggerNextTime, isLessThanOrEqualTo(nextScanTs)) // 下次调度时间小于等于未来5秒
                                .limit(MAX_LIMIT_PRE_READ)
                                .build().render(RenderingStrategies.MYBATIS3)
                );

                // 遍历待执行的任务
                for (JobInfo info : jobInfoList) {
                    Long triggerNextTime = info.getTriggerNextTime();

                    // 1. 过时的任务 - 忽略
                    if (triggerNextTime < nowTimeTs) {
                        LOGGER.warn("任务:{},错失触发时间:{}", info.getId(),
                                DateFormatUtils.format(triggerNextTime, DATE_TIME_PATTERN));

                        // 刷新下次调度时间
                        refreshNextValidTime(info, new Date());
                        continue;
                    }

                    // 2. 当前秒要执行的任务
                    if ((triggerNextTime / 1000 % 60) == (nowTimeTs / 1000 % 60)) {
                        // 立马进行调度
                        JobTriggerPoolHelper.push(info.getId(), TriggerTypeEnum.CRON, -1, null);

                        // 刷新下次调度时间
                        refreshNextValidTime(info, new Date());
                        triggerNextTime = info.getTriggerNextTime();
                    }

                    while (triggerNextTime <= nextScanTs && triggerNextTime != 0L) {
                        int invokeSecond = (int) ((triggerNextTime / 1000) % 60);

                        // 放入执行队列
                        pushInvokeMap(invokeSecond, info.getId());

                        // 刷新下次调度时间
                        refreshNextValidTime(info, new Date());
                        triggerNextTime = info.getTriggerNextTime();
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
                long costMs = System.currentTimeMillis() - startTs;
                LOGGER.info("[SnailJob]-本次任务扫描整理耗时:{}毫秒", costMs);

                // 休眠
                try {
                    TimeUnit.MILLISECONDS.sleep(preReadMs - costMs);
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
