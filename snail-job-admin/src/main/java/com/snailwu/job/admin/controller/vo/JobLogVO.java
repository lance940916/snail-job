package com.snailwu.job.admin.controller.vo;

import com.snailwu.job.admin.model.JobLog;

/**
 * @author 吴庆龙
 * @date 2020/8/17 3:33 下午
 */
public class JobLogVO extends JobLog {

    private String jobName;

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    @Override
    public String toString() {
        return "JobLogVO{" +
                "jobName='" + jobName + '\'' +
                '}';
    }
}