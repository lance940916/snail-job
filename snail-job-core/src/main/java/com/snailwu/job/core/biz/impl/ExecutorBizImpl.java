package com.snailwu.job.core.biz.impl;

import com.snailwu.job.core.biz.ExecutorBiz;
import com.snailwu.job.core.biz.model.IdleBeatParam;
import com.snailwu.job.core.biz.model.KillParam;
import com.snailwu.job.core.biz.model.TriggerParam;
import com.snailwu.job.core.biz.model.ResultT;
import com.snailwu.job.core.enums.ExecutorBlockStrategyEnum;
import com.snailwu.job.core.executor.SnailJobExecutor;
import com.snailwu.job.core.handler.IJobHandler;
import com.snailwu.job.core.thread.JobThread;

/**
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
        JobThread jobThread = SnailJobExecutor.loadJobThread(idleBeatParam.getJobId());
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
        JobThread jobThread = SnailJobExecutor.loadJobThread(triggerParam.getJobId());
        IJobHandler jobHandler = jobThread != null ? jobThread.getJobHandler() : null;
        String removeOldReason = null;

        IJobHandler newJobHandler = SnailJobExecutor.loadJobHandler(triggerParam.getExecutorHandler());
        if (jobThread != null && jobHandler != newJobHandler) {
            removeOldReason = "change jobHandle, and terminate the old job thread.";

            jobThread = null;
            jobHandler = null;
        }

        if (jobHandler == null) {
            jobHandler = newJobHandler;
            if (jobHandler == null) {
                return new ResultT<>(ResultT.FAIL_CODE, "job handler [" + triggerParam.getExecutorHandler() + "] not found.");
            }
        }

        if (jobThread != null) {
            ExecutorBlockStrategyEnum blockStrategy = ExecutorBlockStrategyEnum.match(triggerParam.getExecutorBlockStrategy(), null);
            if (ExecutorBlockStrategyEnum.DISCARD_LATER == blockStrategy) {
                //
                if (jobThread.isRunningOrHasQueue()) {
                    return new ResultT<>(ResultT.FAIL_CODE, "block strategy effect："+ExecutorBlockStrategyEnum.DISCARD_LATER.getTitle());
                }
            } else if (ExecutorBlockStrategyEnum.COVER_EARLY == blockStrategy) {
                if (jobThread.isRunningOrHasQueue()) {
                    removeOldReason = "block strategy effect：" + ExecutorBlockStrategyEnum.COVER_EARLY.getTitle();
                    jobThread = null;
                }
            }
        }

        if (jobThread == null) {
            jobThread = SnailJobExecutor.registryJobThread(triggerParam.getJobId(), jobHandler, removeOldReason);
        }

        // 添加到队列中
        return jobThread.pushTriggerQueue(triggerParam);
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
