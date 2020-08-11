package com.snailwu.job.admin.core.thread;

import com.snailwu.job.admin.trigger.JobTrigger;
import com.snailwu.job.admin.trigger.TriggerTypeEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 将任务的调度放在线程池里进行
 *
 * @author 吴庆龙
 * @date 2020/6/22 11:12 上午
 */
public class JobTriggerPoolHelper {
    private static final Logger LOGGER = LoggerFactory.getLogger(JobTriggerPoolHelper.class);

    private static ThreadPoolExecutor triggerPool;

    /**
     * 启动调度线程
     */
    public static void start() {
        triggerPool = new ThreadPoolExecutor(
                10, 200, 60L, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(1000),
                (ThreadFactory) Thread::new
        );
        // 预先把Core线程启动
        triggerPool.prestartCoreThread();
    }

    /**
     * 停止调度线程
     */
    public static void stop() {
        triggerPool.shutdownNow();
    }

    /**
     * 添加任务到线程池中调度
     * 不指定 executorParam 使用 JobInfo 中的， failRetryCount 同理
     */
    public static void push(int jobId, TriggerTypeEnum triggerType, int failRetryCount, final String executorParam) {
        triggerPool.execute(() -> {
            try {
                JobTrigger.trigger(jobId, triggerType, failRetryCount, executorParam);
            } catch (Exception e) {
                LOGGER.error("任务调度异常", e);
            }
        });
    }

}
