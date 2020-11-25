package com.snailwu.job.admin.controller.entity;

import com.snailwu.job.admin.core.model.JobLog;

public class JobLogVO extends JobLog {
    private String jobName;

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }
}