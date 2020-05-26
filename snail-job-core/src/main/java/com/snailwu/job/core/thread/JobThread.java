package com.snailwu.job.core.thread;

import com.snailwu.job.core.biz.model.ResultT;
import com.snailwu.job.core.biz.model.TriggerParam;
import com.snailwu.job.core.executor.SnailJobExecutor;
import com.snailwu.job.core.handler.IJobHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.*;

/**
 * @author 吴庆龙
 * @date 2020/5/26 12:22 下午
 */
public class JobThread extends Thread {
    public static final Logger log = LoggerFactory.getLogger(JobThread.class);

    private int jobId;
    private IJobHandler jobHandler;
    private LinkedBlockingQueue<TriggerParam> triggerQueue;
    private Set<Long> triggerLogIdSet;

    private volatile boolean toStop = false;
    private String stopReason;

    private boolean running = false;
    private int idleTimes = 0;

    public JobThread(int jobId, IJobHandler jobHandler) {
        this.jobId = jobId;
        this.jobHandler = jobHandler;

        this.triggerQueue = new LinkedBlockingQueue<>();
        this.triggerLogIdSet = Collections.synchronizedSet(new HashSet<>());
    }

    /**
     * 获取 JobHandler
     */
    public IJobHandler getJobHandler() {
        return jobHandler;
    }

    /**
     * 添加到队列中
     */
    public ResultT<String> pushTriggerQueue(TriggerParam triggerParam) {
        if (triggerLogIdSet.contains(triggerParam.getLogId())) {
            log.info("repeat trigger job, logId:{}", triggerParam.getLogId());
            return new ResultT<>(ResultT.FAIL_CODE, "repeat trigger job, logId:" + triggerParam.getLogId());
        }

        triggerLogIdSet.add(triggerParam.getLogId());
        triggerQueue.add(triggerParam);
        return ResultT.SUCCESS;
    }

    /**
     * 线程是否忙碌
     */
    public boolean isRunningOrHasQueue() {
        return running || triggerQueue.size() > 0;
    }

    @Override
    public void run() {
        // init 方法
        try {
            jobHandler.init();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

        while (!toStop) {
            running = false;
            idleTimes++;

            TriggerParam triggerParam = null;
            ResultT<String> executeResult = null;

            try {
                triggerParam = triggerQueue.poll(3L, TimeUnit.SECONDS);
                if (triggerParam != null) { // 有任务需要执行
                    running = true;
                    idleTimes = 0;
                    triggerLogIdSet.remove(triggerParam.getLogId());

                    if (triggerParam.getExecutorTimeout() > 0) {
                        // 有超时时间的
                        Thread futureThead = null;
                        try {
                            final TriggerParam triggerParamTmp = triggerParam;
                            FutureTask<ResultT<String>> futureTask = new FutureTask<>(new Callable<ResultT<String>>() {
                                @Override
                                public ResultT<String> call() throws Exception {
                                    return jobHandler.execute(triggerParamTmp.getExecutorParams());
                                }
                            });

                            futureThead = new Thread(futureTask);
                            futureThead.start();

                            executeResult = futureTask.get(triggerParam.getExecutorTimeout(), TimeUnit.SECONDS);
                        } catch (TimeoutException e) {
                            log.error("snail-job job execute timeout");

                            executeResult = new ResultT<>(ResultT.FAIL_CODE, "job execute timeout");
                        } finally {
                            if (futureThead != null) {
                                futureThead.interrupt();
                            }
                        }
                    } else {
                        // 没有超时时间的
                        executeResult = jobHandler.execute(triggerParam.getExecutorParams());
                    }
                    log.info("snail-job job execute end(finish)");
                } else {
                    if (idleTimes > 30 && triggerQueue.size() == 0) {
                        // 30 次没有任务可以处理自动销毁线程
                        SnailJobExecutor.removeJobThread(jobId, "executor idle time over limit");
                    }
                }
            } catch (Exception e) {
                if (toStop) {
                    log.info("JobThread toStop, stopReason:{}", stopReason);
                }
                log.warn("JobThread 异常结束.", e);
            } finally {
                if (triggerParam != null) {
                    if (!toStop) {
                        // TODO Callback
                    } else {
                        // 线程被杀死了
                        ResultT<String> stopResult = new ResultT<>(ResultT.FAIL_CODE,
                                stopReason + "[job running, killed]");
                        // TODO Callback
                    }
                }
            }
        }

        // 线程被终止，但是还有待执行的任务在队列中
        while (triggerQueue != null && triggerQueue.size() > 0) {
            TriggerParam triggerParam = triggerQueue.poll();
            if (triggerParam != null) {
                ResultT<String> resultT = new ResultT<>(ResultT.FAIL_CODE,
                        stopReason + " [job not executed, in the job queue, killed.]");
                // TODO Callback
            }
        }

        // 销毁
        try {
            jobHandler.destroy();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    /**
     * Thread.interrupt 只支持终止线程的阻塞状态(wait、join、sleep)，
     * 在阻塞出抛出 InterruptedException 异常,但是并不会终止运行的线程本身；
     * 所以需要注意，此处彻底销毁本线程，需要通过共享变量方式；
     */
    public void toStop(String stopReason) {
        this.toStop = true;
        this.stopReason = stopReason;
    }
}
