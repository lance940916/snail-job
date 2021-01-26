package com.snailwu.job.core.biz.model;

/**
 * 执行器是否忙碌参数
 *
 * @author 吴庆龙
 * @date 2020/5/25 12:00 下午
 */
public class IdleBeatParam {

    /**
     * 任务ID
     */
    private int jobId;

    public IdleBeatParam() {
    }

    public IdleBeatParam(int jobId) {
        this.jobId = jobId;
    }

    public int getJobId() {
        return jobId;
    }

    public void setJobId(int jobId) {
        this.jobId = jobId;
    }
}
