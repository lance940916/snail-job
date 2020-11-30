package com.snailwu.job.admin.core.thread;

import com.snailwu.job.admin.core.config.AdminConfig;
import com.snailwu.job.admin.core.cron.CronExpression;
import com.snailwu.job.admin.model.JobInfo;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.mybatis.dynamic.sql.render.RenderingStrategies;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import static com.snailwu.job.admin.constant.JobConstants.DATE_TIME_MS_PATTERN;
import static com.snailwu.job.admin.constant.JobConstants.MAX_LIMIT_PRE_READ;
import static com.snailwu.job.admin.core.trigger.TriggerTypeEnum.CRON;
import static com.snailwu.job.admin.mapper.JobInfoDynamicSqlSupport.*;
import static com.snailwu.job.core.enums.TriggerStatus.RUNNING;
import static com.snailwu.job.core.enums.TriggerStatus.STOPPED;
import static org.mybatis.dynamic.sql.SqlBuilder.*;

/**
 * 定时任务调度类
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
    private static volatile boolean scanJobRunning = true;

    /**
     * 进行调度线程
     */
    private static Thread invokeJobThread;
    private static volatile boolean invokeJobRunning = true;

    /**
     * 缓存将要执行的任务
     * key: 秒
     * val: 任务ID集合
     */
    private static final Map<Long, Set<Integer>> INVOKE_JOB_MAP = new ConcurrentHashMap<>();

    /**
     * 启动线程
     */
    public static void start() {
        scanJobThread = new Thread(() -> {
            while (scanJobRunning) {
                // 当前时间戳
                long nowTimeTs = System.currentTimeMillis();
                try {
                    // 获取 当前时间 + 10秒 内所有待调度的任务
                    long maxTriggerTime = nowTimeTs + 10000;

                    // 扫描将要待执行的任务，根据任务的执行时间从近到远排序
                    List<JobInfo> jobInfos = AdminConfig.getInstance().getJobInfoMapper().selectMany(
                            select(id, cron, triggerNextTime)
                                    .from(jobInfo)
                                    .where(triggerStatus, isEqualTo(RUNNING.getValue()))
                                    .and(triggerNextTime, isLessThanOrEqualTo(maxTriggerTime))
                                    .orderBy(triggerNextTime)
                                    .limit(MAX_LIMIT_PRE_READ)
                                    .build().render(RenderingStrategies.MYBATIS3)
                    );

                    // 遍历待执行的任务
                    for (JobInfo info : jobInfos) {
                        Long triggerNextTime = info.getTriggerNextTime();
                        if (triggerNextTime < nowTimeTs) {
                            // 1. 过时的任务 - 忽略调度并更新下次的执行时间
                            LOGGER.warn("任务：【{}】错失触发时间：{}", info.getId(),
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
                            pushInvokeMap(invokeSecond, info.getId());

                            // 刷新下次调度时间
                            refreshNextValidTime(info, new Date(triggerNextTime));
                            triggerNextTime = info.getTriggerNextTime();
                        }
                    }

                    // 更新任务的下次执行时间
                    for (JobInfo info : jobInfos) {
                        AdminConfig.getInstance().getJobInfoMapper().update(
                                update(jobInfo)
                                        .set(triggerLastTime).equalTo(info.getTriggerLastTime())
                                        .set(triggerNextTime).equalTo(info.getTriggerNextTime())
                                        .set(triggerStatus).equalTo(info.getTriggerStatus())
                                        .where(id, isEqualTo(info.getId()))
                                        .build().render(RenderingStrategies.MYBATIS3)
                        );
                    }
                } catch (Exception e) {
                    LOGGER.error("执行任务调度异常。", e);
                }

                // 计算耗时
                long costMs = System.currentTimeMillis() - nowTimeTs;
                LOGGER.info("本次任务扫描整理耗时：{}毫秒", costMs);
            }
        });
        scanJobThread.setDaemon(true);
        scanJobThread.setName("scan-job-thread");
        scanJobThread.start();
        LOGGER.info("扫描任务线程-已启动。");

        // 执行调度任务线程
        invokeJobThread = new Thread(() -> {
            while (invokeJobRunning) {
                long nowTimeTs = System.currentTimeMillis();

                // 当前的秒
                long curSecond = (nowTimeTs / 1000) % 60;

                // 上一秒要执行的任务（几乎没有上一秒未执行的数据）
                Set<Integer> preJobIds = INVOKE_JOB_MAP.remove(curSecond - 1);
                if (preJobIds != null) {
                    for (Integer jobId : preJobIds) {
                        JobTriggerPoolHelper.push(jobId, CRON, -1, null);
                    }
                }

                // 获取本秒要执行的任务
                Set<Integer> jobIds = INVOKE_JOB_MAP.remove(curSecond);
                if (jobIds != null) {
                    for (Integer jobId : jobIds) {
                        JobTriggerPoolHelper.push(jobId, CRON, -1, null);
                    }
                }

                // 计算耗时。耗时如果大于1秒会造成任务调度时间不准确
                long costMs = System.currentTimeMillis() - nowTimeTs;
                LOGGER.info("本次任务调度耗时时间：{}毫秒", costMs);
                if (costMs > 1000) {
                    LOGGER.warn("本次任务调度耗时时间过长：{}毫秒", costMs);
                }

                // 休眠
                try {
                    TimeUnit.MILLISECONDS.sleep(1000 - costMs);
                } catch (InterruptedException e) {
                    if (scanJobRunning) {
                        LOGGER.error("休眠异常。", e);
                    }
                }
            }
        });
        invokeJobThread.setDaemon(true);
        invokeJobThread.setPriority(Thread.MAX_PRIORITY);
        invokeJobThread.setName("invoke-job-thread");
        invokeJobThread.start();
        LOGGER.info("调度任务线程-已启动。");
    }

    /**
     * 计算任务下次的执行时间
     * TODO 可以对 CronExpression 类进行缓存（淘汰策略为最近未使用的）
     */
    private static void refreshNextValidTime(JobInfo info, Date fromDate) {
        Date nextValidDate = null;
        try {
            nextValidDate = new CronExpression(info.getCron()).getNextValidTimeAfter(fromDate);
        } catch (ParseException e) {
            LOGGER.error("Cron表达式异常。jobId：{}，cron：{}", info.getId(), info.getCron());
        }
        if (nextValidDate == null) {
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
     * 加入到执行队列中
     */
    private static void pushInvokeMap(long invokeSecond, int jobId) {
        Set<Integer> jobIdSet = INVOKE_JOB_MAP.computeIfAbsent(invokeSecond, k -> new HashSet<>());
        jobIdSet.add(jobId);
    }

    /**
     * Stop
     */
    public static void stop() {
        // 设置为停止标志
        scanJobRunning = false;
        scanJobThread.interrupt();
        try {
            scanJobThread.join();
        } catch (InterruptedException e) {
            LOGGER.error(e.getMessage(), e);
        }
        LOGGER.info("扫描任务线程-已停止。");

        // 停止调度线程（未来待调度的任务停止调度）
        invokeJobRunning = false;
        invokeJobThread.interrupt();
        try {
            invokeJobThread.join();
        } catch (InterruptedException e) {
            LOGGER.error(e.getMessage(), e);
        }
        LOGGER.info("调度任务线程-已停止。");
    }

}
