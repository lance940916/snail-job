package com.snailwu.job.core.biz.impl;

import com.snailwu.job.core.biz.ExecutorBiz;
import com.snailwu.job.core.biz.model.IdleBeatParam;
import com.snailwu.job.core.biz.model.KillParam;
import com.snailwu.job.core.biz.model.ResultT;
import com.snailwu.job.core.biz.model.TriggerParam;
import com.snailwu.job.core.executor.SnailJobExecutor;
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
        JobThread jobThread = SnailJobExecutor.loadJobThread(idleBeatParam.getJobId());
        if (jobThread != null && jobThread.isRunningOrHasQueue()) {
            return new ResultT<>(ResultT.FAIL_CODE, "执行器忙碌.");
        }
        return ResultT.SUCCESS;
    }

    @Override
    public ResultT<String> run(TriggerParam triggerParam) {
        // 通过 jobId, 获取执行线程，获取执行方法。如果任务执行了一次后，这里就可以获取到了
        JobThread jobThread = SnailJobExecutor.loadJobThread(triggerParam.getJobId());
        IJobHandler jobHandler = jobThread != null ? jobThread.getJobHandler() : null;

        // 移除原因
        String removeOldReason = null;

        // 与已存在的线程对比线程内的 jobHandler，如果不一样，则动态替换 handler。立即生效
        IJobHandler newJobHandler = SnailJobExecutor.loadJobHandler(triggerParam.getExecutorHandler());
        if (jobThread != null && jobHandler != newJobHandler) {
            removeOldReason = "JobHandler发生变更";
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
            jobThread = SnailJobExecutor.registryJobThread(triggerParam.getJobId(), jobHandler, removeOldReason);
        }

        // 最后 添加任务到队列中等待执行
        return jobThread.addJobQueue(triggerParam);
    }

    @Override
    public ResultT<String> kill(KillParam killParam) {
        JobThread jobThread = SnailJobExecutor.loadJobThread(killParam.getJobId());
        if (jobThread != null) {
            SnailJobExecutor.removeJobThread(killParam.getJobId(), "scheduling center kill job.");
            return ResultT.SUCCESS;
        }
        return new ResultT<>(ResultT.SUCCESS_CODE, "job thread already killed.");
    }
}
