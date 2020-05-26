package com.snailwu.job.core.biz.model;

/**
 * @author 吴庆龙
 * @date 2020/5/25 12:59 下午
 */
public class KillParam {

    private int jobId;

    public KillParam() {
    }

    public KillParam(int jobId) {
        this.jobId = jobId;
    }

    public int getJobId() {
        return jobId;
    }

    public void setJobId(int jobId) {
        this.jobId = jobId;
    }
}
