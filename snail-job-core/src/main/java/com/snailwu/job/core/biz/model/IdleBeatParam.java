package com.snailwu.job.core.biz.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author 吴庆龙
 * @date 2020/5/25 12:00 下午
 */
public class IdleBeatParam {

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
