package com.snailwu.job.admin.model;

import javax.annotation.Generated;
import java.util.Date;

public class JobLog {
    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private Long id;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private Integer jobId;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private String appName;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private String execAddress;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private String execHandler;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private String execParam;

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
    public String getAppName() {
        return appName;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setAppName(String appName) {
        this.appName = appName;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public String getExecAddress() {
        return execAddress;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setExecAddress(String execAddress) {
        this.execAddress = execAddress;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public String getExecHandler() {
        return execHandler;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setExecHandler(String execHandler) {
        this.execHandler = execHandler;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public String getExecParam() {
        return execParam;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setExecParam(String execParam) {
        this.execParam = execParam;
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