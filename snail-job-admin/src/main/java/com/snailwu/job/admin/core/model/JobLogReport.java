package com.snailwu.job.admin.core.model;

import java.util.Date;
import javax.annotation.Generated;

public class JobLogReport {
    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private Integer id;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private Date triggerTime;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private Integer runningCount;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private Integer successCount;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private Integer failCount;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public Integer getId() {
        return id;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setId(Integer id) {
        this.id = id;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public Date getTriggerTime() {
        return triggerTime;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setTriggerTime(Date triggerTime) {
        this.triggerTime = triggerTime;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public Integer getRunningCount() {
        return runningCount;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setRunningCount(Integer runningCount) {
        this.runningCount = runningCount;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public Integer getSuccessCount() {
        return successCount;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setSuccessCount(Integer successCount) {
        this.successCount = successCount;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public Integer getFailCount() {
        return failCount;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setFailCount(Integer failCount) {
        this.failCount = failCount;
    }
}