package com.snailwu.job.core.thread;

import com.snailwu.job.core.biz.model.CallbackParam;
import com.snailwu.job.core.biz.model.ResultT;
import com.snailwu.job.core.biz.model.TriggerParam;
import com.snailwu.job.core.executor.SnailJobExecutor;
import com.snailwu.job.core.handler.IJobHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.*;

/**
 * JobHandler 对应的 Thread
 *
 * @author 吴庆龙
 * @date 2020/5/26 12:22 下午
 */
public class JobThread extends Thread {
    public static final Logger LOGGER = LoggerFactory.getLogger(JobThread.class);

    /**
     * 任务的 id
     */
    private final int jobId;

    /**
     * 任务对应的 Handler
     */
    private final IJobHandler jobHandler;

    /**
     * 该线程要执行的任务队列
     */
    private final LinkedBlockingQueue<TriggerParam> jobQueue;

    /**
     * Job 对应的日志表的 ID
     */
    private final Set<Long> logIdSet;

    /**
     * 是否退出 run 方法的 while 循环
     */
    private volatile boolean stopFlag = false;

    /**
     * 停止 while 循环的原因
     */
    private String stopReason;

    /**
     * 是否正在执行任务
     */
    private boolean runningFlag = false;

    /**
     * 从队列中获取任务失败的次数，达到指定次数后则将该线程停止
     */
    private int idleTimes = 0;

    public JobThread(int jobId, IJobHandler jobHandler) {
        this.jobId = jobId;
        this.jobHandler = jobHandler;

        this.jobQueue = new LinkedBlockingQueue<>();
        this.logIdSet = Collections.synchronizedSet(new HashSet<>());
    }

    /**
     * 获取 JobHandler
     */
    public IJobHandler getJobHandler() {
        return jobHandler;
    }

    /**
     * 添加任务到任务队列中
     */
    public ResultT<String> addLogQueue(TriggerParam triggerParam) {
        if (logIdSet.contains(triggerParam.getLogId())) {
            LOGGER.info("[SnailJob]-执行任务-队列中已有重复的任务, logId:{}", triggerParam.getLogId());
            return new ResultT<>(ResultT.FAIL_CODE, "repeat trigger job, logId:" + triggerParam.getLogId());
        }
        logIdSet.add(triggerParam.getLogId());

        // 添加到执行队列
        jobQueue.add(triggerParam);
        return ResultT.SUCCESS;
    }

    /**
     * 该 JobThread 是否忙碌
     */
    public boolean isRunningOrHasQueue() {
        return runningFlag || jobQueue.size() > 0;
    }

    @Override
    public void run() {
        // 执行该任务的初始化方法
        try {
            jobHandler.init();
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }

        // 线程启动后不断轮训队列，有任务就执行，没有任务则累加次数达到 30 次就将该线程停止
        while (!stopFlag) {
            // 没有运行任务
            runningFlag = false;
            // 累加无任务次数
            idleTimes++;

            // 任务参数
            TriggerParam triggerParam = null;
            // 任务执行结果
            ResultT<String> executeResult = null;

            try {
                // 从队列中获取任务
                triggerParam = jobQueue.poll(3L, TimeUnit.SECONDS);
                if (triggerParam != null) { // 有任务需要执行
                    // 设置为运行任务中
                    runningFlag = true;
                    // 重置无任务次数
                    idleTimes = 0;
                    // 日志集合中移除
                    logIdSet.remove(triggerParam.getLogId());

                    if (triggerParam.getExecutorTimeout() > 0) { // 有超时时间的
                        // 启动线程
                        final TriggerParam triggerParamTmp = triggerParam;
                        FutureTask<ResultT<String>> futureTask = new FutureTask<>(new Callable<ResultT<String>>() {
                            @Override
                            public ResultT<String> call() throws Exception {
                                return jobHandler.execute(triggerParamTmp.getExecutorParams());
                            }
                        });
                        Thread futureThead = new Thread(futureTask);
                        futureThead.start();

                        // 获取执行结果，有超时时间
                        try {
                            executeResult = futureTask.get(triggerParam.getExecutorTimeout(), TimeUnit.SECONDS);
                        } catch (TimeoutException e) {
                            LOGGER.error("[SnailJob]-任务执行超时, jobId:{}", jobId);
                            executeResult = new ResultT<>(ResultT.FAIL_CODE, "任务执行超时");
                        } finally {
                            // 发生异常时，终止线程的挂起操作，让线程自行结束
                            // 线程内如果用 while(true) 的话，interrupt 方法不能停止线程
                            futureThead.interrupt();
                        }
                    } else { // 没有超时时间的，直接执行
                        executeResult = jobHandler.execute(triggerParam.getExecutorParams());
                    }
                    LOGGER.info("任务执行完成");
                } else {
                    // 30 次获取操作后，没有任务可以处理，则自动销毁线程
                    if (idleTimes > 30 && jobQueue.size() == 0) {
                        SnailJobExecutor.removeJobThread(jobId, "执行线程空闲终止");
                    }
                }
            } catch (Exception e) {
                if (stopFlag) {
                    // 线程停止时抛出的异常忽略
                    LOGGER.info("[SnailJob]-任务执行线程停止, 原因:{}", stopReason);
                } else {
                    // 线程未停止时抛出了异常需要打印
                    LOGGER.error("[SnailJob]-任务执行线程异常结束.", e);
                }
            } finally {
                if (triggerParam != null) {
                    CallbackParam callbackParam = new CallbackParam();
                    callbackParam.setLogId(triggerParam.getLogId());
                    callbackParam.setLogDateTime(triggerParam.getLogDateTime());
                    if (!stopFlag) {
                        callbackParam.setExecuteResult(executeResult);
                        TriggerCallbackThread.addCallbackQueue(callbackParam);
                    } else {
                        // 线程被杀死了
                        ResultT<String> stopResult = new ResultT<>(ResultT.FAIL_CODE,
                                stopReason + "[job running, killed]");
                        callbackParam.setExecuteResult(stopResult);
                        TriggerCallbackThread.addCallbackQueue(callbackParam);
                    }
                }
            }
        }

        // 线程被终止，但是还有待执行的任务在队列中
        while (jobQueue.size() > 0) {
            TriggerParam triggerParam = jobQueue.poll();
            if (triggerParam != null) {
                ResultT<String> resultT = new ResultT<>(ResultT.FAIL_CODE,
                        stopReason + " [job not executed, in the job queue, killed.]");
                CallbackParam callbackParam = new CallbackParam(triggerParam.getLogId(), triggerParam.getLogDateTime(), resultT);
                TriggerCallbackThread.addCallbackQueue(callbackParam);
            }
        }

        // 执行该任务的销毁方法
        try {
            jobHandler.destroy();
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    /**
     * Thread.interrupt 只支持终止线程的阻塞状态(wait、join、sleep)，
     * 在阻塞出抛出 InterruptedException 异常,但是并不会终止运行的线程本身；
     * 所以需要注意，此处彻底销毁本线程，需要通过共享变量方式；
     */
    public void toStop(String stopReason) {
        this.stopFlag = true;
        this.stopReason = stopReason;
    }
}
