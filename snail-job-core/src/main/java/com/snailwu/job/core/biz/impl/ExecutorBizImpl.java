package com.snailwu.job.core.biz.impl;

import com.snailwu.job.core.biz.ExecutorBiz;
import com.snailwu.job.core.biz.model.IdleBeatParam;
import com.snailwu.job.core.biz.model.KillParam;
import com.snailwu.job.core.biz.model.ResultT;
import com.snailwu.job.core.biz.model.TriggerParam;
import com.snailwu.job.core.executor.JobExecutor;
import com.snailwu.job.core.handler.IJobHandler;
import com.snailwu.job.core.thread.JobThread;

/**
 * core 包中的方法实现
 *
 * @author 吴庆龙
 * @date 2020/5/25 2:24 下午
 */
public class ExecutorBizImpl implements ExecutorBiz {

    @Override
    public ResultT<String> beat() {
        return ResultT.SUCCESS;
    }

    @Override
    public ResultT<String> idleBeat(IdleBeatParam idleBeatParam) {
        boolean isRunningOrHasQueue = false;
        JobThread jobThread = JobExecutor.loadJobThread(idleBeatParam.getJobId());
        if (jobThread != null && jobThread.isRunningOrHasQueue()) {
            isRunningOrHasQueue = true;
        }

        if (isRunningOrHasQueue) {
            return new ResultT<>(ResultT.FAIL_CODE, "job thread is running or has trigger queue.");
        }
        return ResultT.SUCCESS;
    }

    @Override
    public ResultT<String> run(TriggerParam triggerParam) {
        // 通过 jobId, 获取执行线程，获取执行方法。如果任务执行了一次后，这里就可以获取到了
        JobThread jobThread = JobExecutor.loadJobThread(triggerParam.getJobId());
        IJobHandler jobHandler = jobThread != null ? jobThread.getJobHandler() : null;

        // 移除原因
        String removeOldReason = null;

        // 与已存在的线程对比线程内的 jobHandler，如果不一样，则动态替换 handler，下次就生效了。
        IJobHandler newJobHandler = JobExecutor.loadJobHandler(triggerParam.getExecutorHandler());
        if (jobThread != null && jobHandler != newJobHandler) {
            removeOldReason = "更换了 JobHandler，将旧的 Thread 终止掉。";
            jobThread = null;
            jobHandler = null;
        }

        if (jobHandler == null) {
            // 更换为新的 handler
            jobHandler = newJobHandler;
            if (jobHandler == null) {
                return new ResultT<>(ResultT.FAIL_CODE, "job handler [" + triggerParam.getExecutorHandler() + "] not found.");
            }
        }

        if (jobThread == null) {
            jobThread = JobExecutor.registryJobThread(triggerParam.getJobId(), jobHandler, removeOldReason);
        }

        // 添加到队列中
        return jobThread.pushTriggerQueue(triggerParam);
    }

    @Override
    public ResultT<String> kill(KillParam killParam) {
        JobThread jobThread = JobExecutor.loadJobThread(killParam.getJobId());
        if (jobThread != null) {
            JobExecutor.removeJobThread(killParam.getJobId(), "scheduling center kill job.");
            return ResultT.SUCCESS;
        }
        return new ResultT<>(ResultT.SUCCESS_CODE, "job thread already killed.");
    }
}
