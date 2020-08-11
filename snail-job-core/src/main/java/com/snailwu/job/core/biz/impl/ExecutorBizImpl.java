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
            return new ResultT<>(ResultT.FAIL_CODE, "执行器忙碌");
        }
        return ResultT.SUCCESS;
    }

    @Override
    public ResultT<String> run(TriggerParam triggerParam) {
        Integer jobId = triggerParam.getJobId();

        // 获取任务指定的 job
        String executorHandler = triggerParam.getExecutorHandler();
        IJobHandler newJobHandler = SnailJobExecutor.loadJobHandler(executorHandler);
        // 没有找到对应的 newJobHandler 直接报错
        if (newJobHandler == null) {
            return new ResultT<>(ResultT.FAIL_CODE, "没有指定的JobHandler-[" + executorHandler + "]");
        }

        // 通过 jobId, 获取执行线程
        JobThread jobThread = SnailJobExecutor.loadJobThread(jobId);
        if (jobThread == null) { // 该任务第一次被调度
            // 生成 Job 对应的 jobThread
            jobThread = SnailJobExecutor.registryJobThread(jobId, newJobHandler, null);
        } else { // 任务非第一次被调度
            // 检查 jobHandler 是否发生了变化
            // 如果发生变化，将以前的线程停止，启动一个新的线程
            // 没有发生变化，则直接添加到线程的执行队列中
            IJobHandler jobHandler = jobThread.getJobHandler();
            if (jobHandler != newJobHandler) {
                jobHandler = newJobHandler;
                jobThread = SnailJobExecutor.registryJobThread(jobId, jobHandler, "任务对应的JobHandler发生变化");
            }
        }

        // 最后 添加任务到队列中等待执行
        return jobThread.addJobQueue(triggerParam);
    }

    @Override
    public ResultT<String> kill(KillParam killParam) {
        // 获取调度线程
        JobThread jobThread = SnailJobExecutor.loadJobThread(killParam.getJobId());
        if (jobThread == null) {
            return new ResultT<>(ResultT.SUCCESS_CODE, "调度线程为空.");
        }
        // 移除任务
        return jobThread.removeJobQueue(killParam.convertTriggerParam());
    }
}
