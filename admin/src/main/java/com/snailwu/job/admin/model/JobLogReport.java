package com.snailwu.job.admin.model;

import java.util.Date;

public class JobLogReport {
    private Integer id;

    private Date triggerDay;

    private Integer runningCount;

    private Integer successCount;

    private Integer failCount;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getTriggerDay() {
        return triggerDay;
    }

    public void setTriggerDay(Date triggerDay) {
        this.triggerDay = triggerDay;
    }

    public Integer getRunningCount() {
        return runningCount;
    }

    public void setRunningCount(Integer runningCount) {
        this.runningCount = runningCount;
    }

    public Integer getSuccessCount() {
        return successCount;
    }

    public void setSuccessCount(Integer successCount) {
        this.successCount = successCount;
    }

    public Integer getFailCount() {
        return failCount;
    }

    public void setFailCount(Integer failCount) {
        this.failCount = failCount;
    }
}