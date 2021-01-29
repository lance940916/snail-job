package com.snailwu.job.admin.core.thread;

import com.snailwu.job.admin.core.config.AdminConfig;
import com.snailwu.job.admin.core.cron.CronExpression;
import com.snailwu.job.admin.model.JobInfo;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import static com.snailwu.job.admin.constant.AdminConstants.DATE_TIME_MS_PATTERN;
import static com.snailwu.job.admin.core.trigger.TriggerTypeEnum.CRON;
import static com.snailwu.job.core.enums.TriggerStatus.RUNNING;
import static com.snailwu.job.core.enums.TriggerStatus.STOPPED;

/**
 * 定时任务调度类
 *
 * @author 吴庆龙
 * @date 2020/7/9 2:40 下午
 */
public class JobScheduleHelper {
    private static final Logger logger = LoggerFactory.getLogger(JobScheduleHelper.class);

    /**
     * 扫描可调度的任务线程
     */
    private static Thread scanJobThread;
    private static volatile boolean scanJobRunning = true;

    /**
     * 进行调度线程
     */
    private static Thread triggerJobThread;
    private static volatile boolean triggerJobRunning = true;

    /**
     * 缓存将要执行的任务
     * key: 秒
     * val: 任务ID集合
     */
    private static final Map<Long, Set<Integer>> TRIGGER_JOB_MAP = new ConcurrentHashMap<>();

    private static void scanJob() throws InterruptedException {
        // 当前时间戳
        long nowTimeTs = System.currentTimeMillis();
        // 获取 当前时间 + 10秒 内所有待调度的任务
        long maxTriggerTime = nowTimeTs + 10000;

        // 扫描将要待执行的任务，根据任务的执行时间从近到远排序
        List<JobInfo> jobInfos = AdminConfig.getInstance().getJobInfoMapper()
                .selectTriggerAtTime(RUNNING.getValue(), maxTriggerTime);

        // 遍历待执行的任务
        for (JobInfo info : jobInfos) {
            Long triggerNextTime = info.getTriggerNextTime();
            if (triggerNextTime < nowTimeTs) {
                // 1. 过时的任务 - 忽略调度并更新下次的执行时间
                logger.warn("任务：【{}】错失触发时间：{}", info.getId(),
                        DateFormatUtils.format(triggerNextTime, DATE_TIME_MS_PATTERN));

                // 刷新下次调度时间
                refreshNextValidTime(info, new Date());
            } else if (((triggerNextTime / 1000) % 60) == ((nowTimeTs / 1000) % 60)) {
                // 2. 当前秒要执行的任务, 直接进行调度
                JobTriggerPoolHelper.push(info.getId(), CRON, -1, null);

                // 刷新下次调度时间
                refreshNextValidTime(info, new Date(triggerNextTime));
            }

            // 重新赋值
            triggerNextTime = info.getTriggerNextTime();

            // 3. 在 [当前秒+1秒,当前秒+preReadMs] 之内要执行的调度任务
            while (triggerNextTime <= maxTriggerTime && triggerNextTime != 0L) {
                // 任务在第几秒开始执行
                long invokeSecond = (triggerNextTime / 1000) % 60;

                // 放入执行队列
                Set<Integer> jobIdSet = TRIGGER_JOB_MAP.computeIfAbsent(invokeSecond, k -> new HashSet<>());
                jobIdSet.add(info.getId());

                // 刷新下次调度时间
                refreshNextValidTime(info, new Date(triggerNextTime));
                triggerNextTime = info.getTriggerNextTime();
            }
        }

        // 更新任务的下次执行时间
        for (JobInfo info : jobInfos) {
            AdminConfig.getInstance().getJobInfoMapper().updateNextTimeById(info.getTriggerLastTime(),
                    info.getTriggerNextTime(), info.getTriggerStatus(), info.getId());
        }

        // 休眠，休眠时间小于提前扫描的时间，可以提前扫描到待调度的任务，防止本秒扫描本秒调度的情况发生。
        if (scanJobRunning) {
            TimeUnit.SECONDS.sleep(5);
        }
    }

    /**
     * 在当前秒时进行任务调度
     * 放入到调度线程池中由线程池进行异步执行
     */
    private static void triggerJob() throws InterruptedException {
        long nowTimeTs = System.currentTimeMillis();

        // 当前的秒
        long curSecond = (nowTimeTs / 1000) % 60;

        // 上一秒要执行的任务。
        // 上一秒可能有任务的情况：1、休眠异常，2、调度时间超过一秒
        Set<Integer> preJobIds = TRIGGER_JOB_MAP.remove(curSecond - 1);
        if (preJobIds != null) {
            for (Integer jobId : preJobIds) {
                JobTriggerPoolHelper.push(jobId, CRON);
            }
        }

        // 获取本秒要执行的任务
        Set<Integer> jobIds = TRIGGER_JOB_MAP.remove(curSecond);
        if (jobIds != null) {
            for (Integer jobId : jobIds) {
                JobTriggerPoolHelper.push(jobId, CRON);
            }
        }

        // 计算耗时。耗时如果大于1秒会造成任务调度时间不准确
        long costMs = System.currentTimeMillis() - nowTimeTs;
        if (costMs >= 1000) {
            logger.warn("执行任务调度耗时过长：{}毫秒！！！", costMs);
        }

        // 对齐到整秒，耗时大于一秒，不进行睡眠
        // 本次休眠到下次进行调度，会耗费 5 到 10 毫秒左右，所以任务调度的时间一般会有100毫秒以内的延时。
        if (triggerJobRunning && costMs < 1000) {
            // 本秒留给任务调度的毫秒数
            long leftMs = 1000 - nowTimeTs % 1000;
            // 本秒还剩余多少毫秒，然后进行睡眠
            long sleepMs = leftMs - costMs;
            TimeUnit.MILLISECONDS.sleep(sleepMs);
        }
    }

    /**
     * 启动线程
     */
    public static void start() {
        // 任务扫描线程
        scanJobThread = new Thread(() -> {
            while (scanJobRunning) {
                try {
                    scanJob();
                } catch (InterruptedException e) {
                    if (scanJobRunning) {
                        logger.error("线程：{} 休眠异常。", scanJobThread.getName(), e);
                    }
                } catch (Exception e) {
                    logger.error("执行任务调度异常。", e);
                }
            }
        });
        scanJobThread.setDaemon(true);
        scanJobThread.setName("scan-job");
        scanJobThread.start();

        // 任务调度线程
        triggerJobThread = new Thread(() -> {
            // 开始调度前对齐到整秒
            long curTs = System.currentTimeMillis();
            long sleepTime = 1000 - (curTs % 1000);
            try {
                TimeUnit.MILLISECONDS.sleep(sleepTime);
            } catch (InterruptedException e) {
                // ignore
            }

            // 执行到此时会耗费 5 到 10 毫秒
            while (triggerJobRunning) {
                try {
                    triggerJob();
                } catch (InterruptedException e) {
                    if (triggerJobRunning) {
                        logger.error("线程：{} 休眠异常。", triggerJobThread.getName(), e);
                    }
                } catch (Exception e) {
                    logger.error("执行任务调度异常。", e);
                }
            }
        });
        triggerJobThread.setDaemon(true);
        triggerJobThread.setPriority(Thread.MAX_PRIORITY);
        triggerJobThread.setName("trigger-job");
        triggerJobThread.start();
    }

    /**
     * 计算任务下次的执行时间
     * TODO 对 CronExpression 类使用 LFU 进行缓存
     */
    private static void refreshNextValidTime(JobInfo info, Date fromDate) {
        Date nextValidDate = null;
        try {
            nextValidDate = new CronExpression(info.getCron()).getNextValidTimeAfter(fromDate);
        } catch (ParseException e) {
            logger.error("Cron表达式异常。jobId：{}，cron：{}", info.getId(), info.getCron());
        }
        if (nextValidDate == null) {
            // 使任务停止
            info.setTriggerLastTime(0L);
            info.setTriggerNextTime(0L);
            info.setTriggerStatus(STOPPED.getValue());
        } else {
            info.setTriggerLastTime(info.getTriggerNextTime());
            info.setTriggerNextTime(nextValidDate.getTime());
            info.setTriggerStatus(RUNNING.getValue());
        }
    }

    /**
     * 停止线程
     */
    public static void stop() {
        // 设置为停止标志
        scanJobRunning = false;
        try {
            scanJobThread.interrupt();
            scanJobThread.join();
        } catch (InterruptedException e) {
            logger.error("停止线程 {} 异常", scanJobThread.getName(), e);
        }

        // 停止调度线程
        // TRIGGER_JOB_MAP 中未进行调度的任务停止进行调度
        triggerJobRunning = false;
        try {
            triggerJobThread.interrupt();
            triggerJobThread.join();
        } catch (InterruptedException e) {
            logger.error("停止线程 {} 异常", triggerJobThread.getName(), e);
        }
    }

}
