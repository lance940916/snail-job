package com.snailwu.job.admin.model;

import java.util.Date;

public class JobLog {
    private Long id;

    private Integer jobId;

    private String appName;

    private String execAddress;

    private String execHandler;

    private String execParam;

    private Byte failRetryCount;

    private Date triggerTime;

    private Integer triggerCode;

    private String triggerMsg;

    private Date execTime;

    private Integer execCode;

    private String execMsg;

    private Byte alarmStatus;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getJobId() {
        return jobId;
    }

    public void setJobId(Integer jobId) {
        this.jobId = jobId;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getExecAddress() {
        return execAddress;
    }

    public void setExecAddress(String execAddress) {
        this.execAddress = execAddress;
    }

    public String getExecHandler() {
        return execHandler;
    }

    public void setExecHandler(String execHandler) {
        this.execHandler = execHandler;
    }

    public String getExecParam() {
        return execParam;
    }

    public void setExecParam(String execParam) {
        this.execParam = execParam;
    }

    public Byte getFailRetryCount() {
        return failRetryCount;
    }

    public void setFailRetryCount(Byte failRetryCount) {
        this.failRetryCount = failRetryCount;
    }

    public Date getTriggerTime() {
        return triggerTime;
    }

    public void setTriggerTime(Date triggerTime) {
        this.triggerTime = triggerTime;
    }

    public Integer getTriggerCode() {
        return triggerCode;
    }

    public void setTriggerCode(Integer triggerCode) {
        this.triggerCode = triggerCode;
    }

    public String getTriggerMsg() {
        return triggerMsg;
    }

    public void setTriggerMsg(String triggerMsg) {
        this.triggerMsg = triggerMsg;
    }

    public Date getExecTime() {
        return execTime;
    }

    public void setExecTime(Date execTime) {
        this.execTime = execTime;
    }

    public Integer getExecCode() {
        return execCode;
    }

    public void setExecCode(Integer execCode) {
        this.execCode = execCode;
    }

    public String getExecMsg() {
        return execMsg;
    }

    public void setExecMsg(String execMsg) {
        this.execMsg = execMsg;
    }

    public Byte getAlarmStatus() {
        return alarmStatus;
    }

    public void setAlarmStatus(Byte alarmStatus) {
        this.alarmStatus = alarmStatus;
    }
}