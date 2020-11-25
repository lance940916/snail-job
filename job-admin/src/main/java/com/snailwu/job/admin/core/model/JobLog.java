package com.snailwu.job.admin.core.model;

import javax.annotation.Generated;
import java.util.Date;

public class JobLog {
    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private Long id;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private Integer jobId;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private String groupName;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private String executorAddress;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private String executorHandler;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private String executorParam;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private Byte failRetryCount;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private Date triggerTime;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private Integer triggerCode;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private String triggerMsg;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private Date execTime;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private Integer execCode;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private String execMsg;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private Byte alarmStatus;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public Long getId() {
        return id;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setId(Long id) {
        this.id = id;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public Integer getJobId() {
        return jobId;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setJobId(Integer jobId) {
        this.jobId = jobId;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public String getGroupName() {
        return groupName;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public String getExecutorAddress() {
        return executorAddress;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setExecutorAddress(String executorAddress) {
        this.executorAddress = executorAddress;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public String getExecutorHandler() {
        return executorHandler;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setExecutorHandler(String executorHandler) {
        this.executorHandler = executorHandler;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public String getExecutorParam() {
        return executorParam;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setExecutorParam(String executorParam) {
        this.executorParam = executorParam;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public Byte getFailRetryCount() {
        return failRetryCount;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setFailRetryCount(Byte failRetryCount) {
        this.failRetryCount = failRetryCount;
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
    public Integer getTriggerCode() {
        return triggerCode;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setTriggerCode(Integer triggerCode) {
        this.triggerCode = triggerCode;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public String getTriggerMsg() {
        return triggerMsg;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setTriggerMsg(String triggerMsg) {
        this.triggerMsg = triggerMsg;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public Date getExecTime() {
        return execTime;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setExecTime(Date execTime) {
        this.execTime = execTime;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public Integer getExecCode() {
        return execCode;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setExecCode(Integer execCode) {
        this.execCode = execCode;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public String getExecMsg() {
        return execMsg;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setExecMsg(String execMsg) {
        this.execMsg = execMsg;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public Byte getAlarmStatus() {
        return alarmStatus;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setAlarmStatus(Byte alarmStatus) {
        this.alarmStatus = alarmStatus;
    }
}