package com.snailwu.job.admin.core.thread;

import com.snailwu.job.admin.trigger.JobTrigger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

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

    private ThreadPoolExecutor triggerPool;

    /**
     * 启动调度线程
     */
    public void start() {
        triggerPool = new ThreadPoolExecutor(
                10, 200, 60L, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(1000),
                r -> new Thread(r, "TriggerPool-" + r.hashCode())
        );
        log.info("启动 TriggerPool 成功");
    }

    /**
     * 停止调度线程
     */
    public void stop() {
        triggerPool.shutdownNow();
        log.info("停止 TriggerPool 成功");
    }

    /**
     * 添加任务到线程池中调度
     */
    public void add(final int jobId, final int failRetryCount, final String executorParam) {
        triggerPool.execute(() -> {
            try {
                JobTrigger.trigger(jobId, failRetryCount, executorParam);
            } catch (Exception e) {
                log.error("触发任务异常", e);
            }
        });
    }

}
