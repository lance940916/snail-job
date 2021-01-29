package com.snailwu.job.admin.core.thread;

import com.snailwu.job.admin.core.trigger.JobTrigger;
import com.snailwu.job.admin.core.trigger.TriggerTypeEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 将任务的调度放在线程池里进行
 *
 * @author 吴庆龙
 * @date 2020/6/22 11:12 上午
 */
public class JobTriggerPoolHelper {
    private static final Logger logger = LoggerFactory.getLogger(JobTriggerPoolHelper.class);

    /**
     * 调度池
     */
    private static ThreadPoolExecutor triggerPool;

    /**
     * 启动
     */
    public static void start() {
        triggerPool = new ThreadPoolExecutor(
                10, 200, 60L, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(1000),
                new JobDefaultThreadFactory()
        );
    }

    /**
     * 停止
     */
    public static void stop() {
        triggerPool.shutdownNow();
    }

    /**
     * 添加任务到线程池中调度
     *
     * @param jobId       任务的id
     * @param triggerType 触发类型
     */
    public static void push(int jobId, TriggerTypeEnum triggerType) {
        push(jobId, triggerType, null, null);
    }

    /**
     * 添加任务到线程池中调度
     *
     * @param jobId                  任务的id
     * @param triggerType            触发类型
     * @param overrideFailRetryCount 如果指定则使用该值，-1表示不指定
     * @param overrideExecParam      如果指定则使用改制，null表示不指定
     */
    public static void push(int jobId, TriggerTypeEnum triggerType, Integer overrideFailRetryCount,
                            String overrideExecParam) {
        triggerPool.execute(() -> {
            try {
                JobTrigger.trigger(jobId, triggerType, overrideFailRetryCount, overrideExecParam);
            } catch (Exception e) {
                logger.error("任务调度异常", e);
            }
        });
    }

    /**
     * The default thread factory
     */
    static class JobDefaultThreadFactory implements ThreadFactory {
        private static final AtomicInteger POOL_NUMBER = new AtomicInteger(1);
        private final ThreadGroup group;
        private final AtomicInteger threadNumber = new AtomicInteger(1);
        private final String namePrefix;

        JobDefaultThreadFactory() {
            SecurityManager s = System.getSecurityManager();
            group = (s != null) ? s.getThreadGroup() : Thread.currentThread().getThreadGroup();
            namePrefix = "job-trigger-pool-" + POOL_NUMBER.getAndIncrement() + "-thread-";
        }

        @Override
        public Thread newThread(Runnable r) {
            Thread t = new Thread(group, r, namePrefix + threadNumber.getAndIncrement(), 0);
            if (t.isDaemon()) {
                t.setDaemon(false);
            }
            if (t.getPriority() != Thread.NORM_PRIORITY) {
                t.setPriority(Thread.NORM_PRIORITY);
            }
            return t;
        }
    }

}
