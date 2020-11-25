package com.snailwu.job.core.thread;

import com.snailwu.job.core.biz.model.CallbackParam;
import com.snailwu.job.core.biz.model.ResultT;
import com.snailwu.job.core.biz.model.TriggerParam;
import com.snailwu.job.core.executor.SnailJobExecutor;
import com.snailwu.job.core.handler.IJobHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.Date;
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
     * 做任务去重使用,任务执行一次会生成一次log
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
    public ResultT<String> addJobQueue(TriggerParam triggerParam) {
        // 校验是否重复执行
        if (logIdSet.contains(triggerParam.getLogId())) {
            return new ResultT<>(ResultT.FAIL_CODE, "重复的任务调度.logId:" + triggerParam.getLogId());
        }
        logIdSet.add(triggerParam.getLogId());

        // 添加到执行队列
        jobQueue.add(triggerParam);
        return ResultT.SUCCESS;
    }

    /**
     * 将任务从队列中移除
     * jobId 和 logId 一样即相等
     */
    public ResultT<String> removeJobQueue(TriggerParam triggerParam) {
        // 移除时需要获取读写锁，使用 equals 寻找一样的任务进行移除
        boolean removeResult = jobQueue.remove(triggerParam);
        if (removeResult) {
            // 从判重集合中移除 logId（任务如果成功移除，logIdSet 中的 logId 一定没有被移除）
            logIdSet.add(triggerParam.getLogId());
            return ResultT.SUCCESS;
        }
        return new ResultT<>(ResultT.FAIL_CODE, "任务已经被执行");
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
            LOGGER.error("[SnailJob]-执行任务初始方法异常.原因:{}", e.getMessage());
        }

        // 线程启动后不断轮训队列，有任务就执行，没有任务则累加次数达到 30 次就将该线程停止
        while (!stopFlag) {
            // 累加无任务次数
            idleTimes++;

            // 任务参数
            TriggerParam triggerParam = null;
            // 任务执行结果
            ResultT<String> execResult = null;

            try {
                // 从队列中获取任务
                triggerParam = jobQueue.poll(3L, TimeUnit.SECONDS);
                if (triggerParam != null) {
                    // 执行任务
                    runningFlag = true;
                    idleTimes = 0;
                    logIdSet.remove(triggerParam.getLogId());

                    // 进行方法的调用
                    execResult = doInvoke(triggerParam);

                    runningFlag = false;
                } else {
                    // 30 次获取操作后，没有任务可以处理，则自动销毁线程
                    if (idleTimes > 30 && jobQueue.size() == 0) {
                        SnailJobExecutor.removeJobThread(jobId, "执行线程空闲终止");
                    }
                }
            } catch (Exception e) {
                if (stopFlag) {
                    // 线程停止时抛出的异常忽略
                    LOGGER.info("[SnailJob]-任务执行线程停止.原因:{}", stopReason);
                } else {
                    // 线程未停止时抛出了异常需要打印
                    LOGGER.error("[SnailJob]-任务执行线程异常结束.原因:{}", e.getMessage());
                }
            } finally {
                // 进行任务执行结果的回调
                if (triggerParam != null && execResult != null) {
                    CallbackParam callbackParam = new CallbackParam();
                    callbackParam.setLogId(triggerParam.getLogId());
                    callbackParam.setExecTime(new Date());
                    callbackParam.setExecCode(execResult.getCode());
                    callbackParam.setExecMsg(execResult.getMsg());
                    ResultCallbackThread.addCallbackQueue(callbackParam);
                }
            }
        }

        // 线程被终止，但是还有待执行的任务在队列中
        while (jobQueue.size() > 0) {
            TriggerParam triggerParam = jobQueue.poll();
            if (triggerParam != null) {
                CallbackParam callbackParam = new CallbackParam();
                callbackParam.setLogId(triggerParam.getLogId());
                callbackParam.setExecTime(new Date());
                callbackParam.setExecCode(ResultT.FAIL_CODE);
                if (stopReason != null) {
                    callbackParam.setExecMsg("任务未执行,执行线程被中断-原因:" + stopReason);
                } else {
                    callbackParam.setExecMsg("任务未执行,执行线程被中断.");
                }
                ResultCallbackThread.addCallbackQueue(callbackParam);
            }
        }

        // 执行该任务的销毁方法
        try {
            jobHandler.destroy();
        } catch (Exception e) {
            LOGGER.error("[SnailJob]-执行任务销毁方法异常.原因:{}", e.getMessage());
        }
    }

    /**
     * 执行方法
     */
    private ResultT<String> doInvoke(TriggerParam triggerParam) throws Exception {
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
                return futureTask.get(triggerParam.getExecutorTimeout(), TimeUnit.SECONDS);
            } catch (TimeoutException e) {
                LOGGER.error("[SnailJob]-任务执行超时.jobId:{}", jobId);
                return new ResultT<>(ResultT.FAIL_CODE, "任务执行超时");
            } finally {
                // 发生异常时，终止线程的挂起操作，让线程自行结束
                // 线程内如果用 while(true) 的话，interrupt 方法不能停止线程
                futureThead.interrupt();
            }
        } else { // 没有超时时间的，直接执行
            return jobHandler.execute(triggerParam.getExecutorParams());
        }
    }

    /**
     * Thread.interrupt 只支持终止线程的阻塞状态(wait、join、sleep)
     * 在阻塞出抛出 InterruptedException 异常,但是并不会终止运行的线程本身
     * 所以需要注意，此处彻底销毁本线程，需要通过共享变量方式
     */
    public void toStop(String stopReason) {
        this.stopFlag = true;
        this.stopReason = stopReason;
    }
}