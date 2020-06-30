package com.snailwu.job.admin.core.thread;

import com.snailwu.job.admin.trigger.TriggerTypeEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author 吴庆龙
 * @date 2020/6/22 11:12 上午
 */
public class JobTriggerPoolHelper {
    private static final Logger log = LoggerFactory.getLogger(JobTriggerPoolHelper.class);

    private ThreadPoolExecutor fastTriggerPool;
    private ThreadPoolExecutor slowTriggerPool;

    public void start() {
        fastTriggerPool = new ThreadPoolExecutor(
                10, 200, 60L, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(1000),
                r -> new Thread(r, "fastTriggerPool-" + r.hashCode())
        );
        slowTriggerPool = new ThreadPoolExecutor(
                10, 100, 60L, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(2000),
                r -> new Thread(r, "slowTriggerPool-" + r.hashCode())
        );
    }

    public void stop() {
        fastTriggerPool.shutdownNow();
        slowTriggerPool.shutdownNow();
        log.info("JobTriggerPool shutdown success.");
    }

    private volatile long minTim = System.currentTimeMillis() / 60000; // 毫秒 -> 分
    private volatile ConcurrentHashMap<Integer, AtomicInteger> jobTimeoutCountMap = new ConcurrentHashMap<>();

    /**
     * add Trigger
     */
    public void addTrigger(final int jobId, final TriggerTypeEnum triggerType, final int failRetryCount,
                           final String executorShardingParam, final String executorParam, final String addressList) {
        ThreadPoolExecutor triggerPool = fastTriggerPool;
        AtomicInteger jobTimeoutCount = jobTimeoutCountMap.get(jobId);
        if (jobTimeoutCount != null && jobTimeoutCount.get() > 10) {
            triggerPool = slowTriggerPool;
        }

        triggerPool.execute(() -> {
            long start = System.currentTimeMillis();


        });

    }


}
