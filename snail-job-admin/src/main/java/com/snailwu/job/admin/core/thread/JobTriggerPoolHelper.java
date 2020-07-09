package com.snailwu.job.admin.core.thread;

import com.snailwu.job.admin.trigger.JobTrigger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 将任务调度提交到线程中进行执行
 *
 * @author 吴庆龙
 * @date 2020/6/22 11:12 上午
 */
public class JobTriggerPoolHelper {
    private static final Logger log = LoggerFactory.getLogger(JobTriggerPoolHelper.class);

    private static final JobTriggerPoolHelper helper = new JobTriggerPoolHelper();

    public static JobTriggerPoolHelper getInstance() {
        return helper;
    }

    private ThreadPoolExecutor fastTriggerPool;
    private ThreadPoolExecutor slowTriggerPool;

    public void start() {
        fastTriggerPool = new ThreadPoolExecutor(
                10, 200, 60L, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(1000),
                r -> new Thread(r, "FastTriggerPool-" + r.hashCode())
        );
        log.info("启动 fastTriggerPool 成功");
        slowTriggerPool = new ThreadPoolExecutor(
                10, 100, 60L, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(2000),
                r -> new Thread(r, "SlowTriggerPool-" + r.hashCode())
        );
        log.info("启动 slowTriggerPool 成功");
    }

    public void stop() {
        fastTriggerPool.shutdownNow();
        slowTriggerPool.shutdownNow();
        log.info("JobTriggerPool shutdown success.");
    }

    // 任务超时统计，实际上只统计到将任务放入到执行器的任务队列中的耗时
    private volatile long minTim = System.currentTimeMillis() / 60000; // 毫秒 -> 分
    private final ConcurrentHashMap<Integer, AtomicInteger> jobTimeoutCountMap = new ConcurrentHashMap<>();

    /**
     * add Trigger
     */
    public void addTriggerPool(final int jobId, final int failRetryCount, final String executorParam) {
        ThreadPoolExecutor triggerPool = fastTriggerPool;
        AtomicInteger jobTimeoutCount = jobTimeoutCountMap.get(jobId);
        if (jobTimeoutCount != null && jobTimeoutCount.get() > 10) {
            triggerPool = slowTriggerPool;
        }

        // 触发
        triggerPool.execute(() -> {
            long startTs = System.currentTimeMillis();
            try {
                JobTrigger.trigger(jobId, failRetryCount, executorParam);
            } catch (Exception e) {
                log.error("触发任务异常", e);
            } finally {
                long minTimNow = System.currentTimeMillis() / 60000;
                if (minTim != minTimNow) {
                    minTim = minTimNow;
                    jobTimeoutCountMap.clear();
                }

                // 任务
                long cost = System.currentTimeMillis() - startTs;
                if (cost > 500) {
                    AtomicInteger timeoutCount = jobTimeoutCountMap.putIfAbsent(jobId, new AtomicInteger(1));
                    if (timeoutCount != null) {
                        timeoutCount.incrementAndGet();
                    }
                }
            }
        });
    }

}
