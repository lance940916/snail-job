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
 * 执行器接口逻辑的实现
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
        JobThread jobThread = JobExecutor.loadJobThread(idleBeatParam.getJobId());
        if (jobThread != null && jobThread.isRunningOrHasQueue()) {
            return new ResultT<>(ResultT.FAIL_CODE, "执行器忙碌");
        }
        return ResultT.SUCCESS;
    }

    @Override
    public ResultT<String> run(TriggerParam triggerParam) {
        // 如果 jobId 对应的 jobName 发生了变化，则需要将之前的线程停止掉，重新开启新线程

        // jobId
        Integer jobId = triggerParam.getJobId();

        // 获取任务指定的 job
        String jobName = triggerParam.getExecHandler();
        IJobHandler paramJobHandler = JobExecutor.loadJobHandler(jobName);
        // 没有找到对应的 paramJobHandler 直接报错
        if (paramJobHandler == null) {
            return new ResultT<>(ResultT.FAIL_CODE, "没有指定的JobHandler-[" + jobName + "]");
        }

        // 通过 jobId, 获取执行线程
        JobThread jobThread = JobExecutor.loadJobThread(jobId);
        if (jobThread == null) {
            // 该任务第一次被调度
            // 生成 Job 对应的 jobThread
            jobThread = JobExecutor.registryAndStartJobThread(jobId, paramJobHandler);
        } else {
            // 任务非第一次被调度
            // 检查 jobHandler 是否发生了变化
            // 如果发生变化，将以前的线程停止，启动一个新的线程
            // 没有发生变化，则直接添加到线程的执行队列中
            IJobHandler jobHandler = jobThread.getJobHandler();
            if (jobHandler != paramJobHandler) {
                JobExecutor.removeAndStopJobThread(jobId, "任务对应的JobHandler发生变化");
                jobThread = JobExecutor.registryAndStartJobThread(jobId, paramJobHandler);
            }
        }

        // 最后 添加任务到队列中等待执行
        return jobThread.addJobQueue(triggerParam);
    }

    @Override
    public ResultT<String> kill(KillParam killParam) {
        // 获取调度线程
        JobThread jobThread = JobExecutor.loadJobThread(killParam.getJobId());
        if (jobThread == null) {
            return new ResultT<>(ResultT.FAIL_CODE, "任务对应的调度线程不存在.");
        }
        // 移除任务
        return jobThread.removeJobQueue(killParam.convertTriggerParam());
    }
}
