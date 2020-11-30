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
    private static final Logger LOGGER = LoggerFactory.getLogger(JobTriggerPoolHelper.class);

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

        // 预先把Core线程启动
        triggerPool.prestartCoreThread();
        LOGGER.info("调度线程池-已启动。");
    }

    /**
     * 停止
     */
    public static void stop() {
        triggerPool.shutdownNow();
        LOGGER.info("调度线程池-已停止。");
    }

    /**
     * 添加任务到线程池中调度
     * 不指定 execParam 使用 JobInfo 中的， failRetryCount 同理
     */
    public static void push(int jobId, TriggerTypeEnum triggerType, int failRetryCount, String execParam) {
        triggerPool.execute(() -> {
            try {
                JobTrigger.trigger(jobId, triggerType, failRetryCount, execParam);
            } catch (Exception e) {
                LOGGER.error("任务调度异常", e);
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
            namePrefix = "job-trigger-pool-" +
                    POOL_NUMBER.getAndIncrement() +
                    "-thread-";
        }

        @Override
        public Thread newThread(Runnable r) {
            Thread t = new Thread(group, r,
                    namePrefix + threadNumber.getAndIncrement(),
                    0);
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
