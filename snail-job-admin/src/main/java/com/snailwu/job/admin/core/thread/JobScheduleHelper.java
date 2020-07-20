package com.snailwu.job.admin.core.thread;

import com.snailwu.job.admin.core.conf.AdminConfig;
import com.snailwu.job.admin.core.cron.CronExpression;
import com.snailwu.job.admin.core.model.JobInfo;
import com.snailwu.job.admin.trigger.TriggerTypeEnum;
import org.mybatis.dynamic.sql.render.RenderingStrategies;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import static com.snailwu.job.admin.mapper.JobInfoDynamicSqlSupport.*;
import static com.snailwu.job.core.enums.TriggerStatus.START;
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
    private static final Logger log = LoggerFactory.getLogger(JobScheduleHelper.class);

    private static Thread scheduleThread;
    private static Thread ringThread;
    private static volatile boolean scheduleStopFlag = false;
    private static volatile boolean ringStopFlag = false;

    /**
     * 提前 5 秒加载数据
     */
    private static final long PRE_READ_MS = 5000;

    /**
     * 缓存将要执行的任务
     * key: 秒
     * val: 任务ID集合
     */
    private static final Map<Integer, List<Integer>> SECOND_JOB_ID_LIST_MAP = new ConcurrentHashMap<>();

    /**
     * 启动线程
     */
    public static void start() {
        scheduleThread = new Thread(() -> {
            // 对齐整秒
            try {
                TimeUnit.MILLISECONDS.sleep(1000 - System.currentTimeMillis() % 1000);
            } catch (InterruptedException e) {
                log.error("对齐整秒，休眠异常", e);
            }
            log.info("初始化 Schedule Thread 成功");

            // 一次取多少个任务，最大支持多少个任务并发执行
            int jobLimit = 200;

            while (!scheduleStopFlag) {
                long startTs = System.currentTimeMillis();

                // TODO 使用数据库锁进行分布式事物的调度

                boolean preReadSuccess = false;
                try {
                    // 当前时间
                    long nowTimeTs = System.currentTimeMillis();

                    // 预读取任务
                    List<JobInfo> jobInfoList = AdminConfig.getInstance().getJobInfoMapper().selectMany(
                            select(jobInfo.allColumns())
                                    .from(jobInfo)
                                    .where(triggerStatus, isEqualTo((byte) 1))
                                    .and(triggerNextTime, isLessThanOrEqualTo(nowTimeTs + PRE_READ_MS))
                                    .limit(jobLimit)
                                    .build().render(RenderingStrategies.MYBATIS3)
                    );
                    // 是否预读到数据
                    preReadSuccess = !jobInfoList.isEmpty();

                    // 遍历任务，进行调度
                    for (JobInfo info : jobInfoList) {
                        if (info.getTriggerNextTime() + PRE_READ_MS < nowTimeTs) {
                            // 还没有达到任务的执行时间
                            log.warn("还未达到任务的执行时间. jobId:{}", info.getId());

                            // 计算任务下次的执行时间
                            refreshNextValidTime(info, new Date());

                        } else if (info.getTriggerNextTime() < nowTimeTs) {
                            // 任务的执行时间在 nowTimeTs-PRE_READ_MS 到 nowTimeTs 之间

                            // 添加任务
                            JobTriggerPoolHelper.push(info.getId(), TriggerTypeEnum.CRON, -1, null);

                            // 计算任务下次的执行时间
                            refreshNextValidTime(info, new Date());

                            // 计算任务的下次触发时间是否在 5 秒内
                            if (START.getValue().equals(info.getTriggerStatus())
                                    && nowTimeTs + PRE_READ_MS > info.getTriggerNextTime()) {

                                // 距离任务的触发时间还有多少秒
                                int ringSecond = (int) ((info.getTriggerNextTime() / 1000) % 60);

                                //
                                pushTimeRing(ringSecond, info.getId());

                                // 计算任务下次的执行时间
                                refreshNextValidTime(info, new Date(info.getTriggerNextTime()));
                            }
                        } else {
                            // 任务执行时间 > nowTimeTs，任务执行时间比当前时间大了

                            // 距离任务的触发时间还有多少秒
                            int ringSecond = (int) ((info.getTriggerNextTime() / 1000) % 60);

                            pushTimeRing(ringSecond, info.getId());

                            // 计算任务下次的执行时间
                            refreshNextValidTime(info, new Date(info.getTriggerNextTime()));
                        }
                    }

                    // 更新任务信息
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
                } catch (ParseException e) {
                    if (!scheduleStopFlag) {
                        log.error("JobScheduleHelper Thread Error.", e);
                    }
                }
                long costTs = System.currentTimeMillis() - startTs;
                log.info("本次调度耗时: {}", costTs);

                // 调度时间小于 1 秒，过度到下一秒
                if (costTs < 1000) {
                    // 对齐整秒
                    try {
                        TimeUnit.MILLISECONDS.sleep((preReadSuccess ? 1000 : PRE_READ_MS) - System.currentTimeMillis() % 1000);
                    } catch (InterruptedException e) {
                        if (!scheduleStopFlag) {
                            log.error("对齐整秒，休眠异常", e);
                        }
                    }
                }
            }
        });
        scheduleThread.setDaemon(true);
        scheduleThread.setName("ScheduleThread");
        scheduleThread.start();

        // 执行调度任务线程
        ringThread = new Thread(() -> {
            // 对齐整秒
            try {
                TimeUnit.MILLISECONDS.sleep(1000 - System.currentTimeMillis() % 1000);
            } catch (InterruptedException e) {
                log.error("对齐整秒，休眠异常", e);
            }

            while (!ringStopFlag) {
                // 当前的秒
                int nowSecond = Calendar.getInstance().get(Calendar.SECOND);

                // 获取本秒以及前一秒的所有 jobId
                List<Integer> jobIdList = new ArrayList<>();
                for (int i = 0; i < 2; i++) {
                    List<Integer> list = SECOND_JOB_ID_LIST_MAP.remove((nowSecond + 60 - i) % 60);
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
                    if (!ringStopFlag) {
                        log.error("对齐整秒，休眠异常", e);
                    }
                }
            }
        });
        ringThread.setDaemon(true);
        ringThread.setName("RingThread");
        ringThread.start();
    }

    /**
     * 计算任务下次的执行时间
     */
    private static void refreshNextValidTime(JobInfo info, Date fromDate) throws ParseException {
        Date nextValidDate = new CronExpression(info.getCron()).getNextValidTimeAfter(fromDate);
        if (nextValidDate != null) {
            info.setTriggerLastTime(info.getTriggerNextTime());
            info.setTriggerNextTime(nextValidDate.getTime());
        } else {
            info.setTriggerStatus((byte) 0);
            info.setTriggerLastTime((long) 0);
            info.setTriggerNextTime((long) 0);
        }
    }

    /**
     * 加入到执行队列中
     */
    private static void pushTimeRing(int ringSecond, int jobId) {
        List<Integer> ringList = SECOND_JOB_ID_LIST_MAP.computeIfAbsent(ringSecond, k -> new ArrayList<>());
        ringList.add(jobId);
    }

    public static void stop() {
        // 设置为停止标志
        scheduleStopFlag = true;
        // 休眠一秒，如果线程主动执行完毕，则正常停止
        try {
            TimeUnit.MILLISECONDS.sleep(1000 - System.currentTimeMillis() % 1000);
        } catch (InterruptedException e) {
            log.error(e.getMessage(), e);
        }
        if (scheduleThread.getState() != Thread.State.TERMINATED) {
            scheduleThread.interrupt();
            try {
                scheduleThread.join();
            } catch (InterruptedException e) {
                log.error(e.getMessage(), e);
            }
        }

        // 调度队列中是否有未进行调度的数据
        if (!SECOND_JOB_ID_LIST_MAP.isEmpty()) {
            try {
                TimeUnit.SECONDS.sleep(8);
            } catch (InterruptedException e) {
                log.error(e.getMessage(), e);
            }
        }

        ringStopFlag = true;
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            log.error(e.getMessage(), e);
        }
        if (ringThread.getState() != Thread.State.TERMINATED) {
            ringThread.interrupt();
            try {
                ringThread.join();
            } catch (InterruptedException e) {
                log.error(e.getMessage(), e);
            }
        }

        log.info("JobScheduleHelper Stop.");
    }
}
