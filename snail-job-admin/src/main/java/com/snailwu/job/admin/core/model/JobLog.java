package com.snailwu.job.admin.core.model;

import java.util.Date;
import javax.annotation.Generated;

public class JobLog {
    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private Long id;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private Integer executorId;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private Integer jobId;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private String executorNodeAddress;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private String executorHandler;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private String executorParam;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private String executorShardingParam;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private Byte executorFailRetryCount;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private Date triggerTime;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private Integer triggerCode;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private Date execTime;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private Integer execCode;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private Byte alarmStatus;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private String triggerMsg;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private String execLog;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public Long getId() {
        return id;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setId(Long id) {
        this.id = id;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public Integer getExecutorId() {
        return executorId;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setExecutorId(Integer executorId) {
        this.executorId = executorId;
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
    public String getExecutorNodeAddress() {
        return executorNodeAddress;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setExecutorNodeAddress(String executorNodeAddress) {
        this.executorNodeAddress = executorNodeAddress;
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
    public String getExecutorShardingParam() {
        return executorShardingParam;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setExecutorShardingParam(String executorShardingParam) {
        this.executorShardingParam = executorShardingParam;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public Byte getExecutorFailRetryCount() {
        return executorFailRetryCount;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setExecutorFailRetryCount(Byte executorFailRetryCount) {
        this.executorFailRetryCount = executorFailRetryCount;
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
    public Byte getAlarmStatus() {
        return alarmStatus;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setAlarmStatus(Byte alarmStatus) {
        this.alarmStatus = alarmStatus;
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
    public String getExecLog() {
        return execLog;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setExecLog(String execLog) {
        this.execLog = execLog;
    }
}