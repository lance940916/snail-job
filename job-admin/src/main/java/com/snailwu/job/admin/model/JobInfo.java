package com.snailwu.job.admin.model;

import java.util.Date;

public class JobInfo {
    private Integer id;

    private String jobName;

    private String appName;

    private String cron;

    private Date createTime;

    private Date updateTime;

    private String author;

    private String alarmEmail;

    private String execRouteStrategy;

    private String execHandler;

    private String execParam;

    private Integer execTimeout;

    private Byte execFailRetryCount;

    private Byte triggerStatus;

    private Long triggerLastTime;

    private Long triggerNextTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getCron() {
        return cron;
    }

    public void setCron(String cron) {
        this.cron = cron;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getAlarmEmail() {
        return alarmEmail;
    }

    public void setAlarmEmail(String alarmEmail) {
        this.alarmEmail = alarmEmail;
    }

    public String getExecRouteStrategy() {
        return execRouteStrategy;
    }

    public void setExecRouteStrategy(String execRouteStrategy) {
        this.execRouteStrategy = execRouteStrategy;
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

    public Integer getExecTimeout() {
        return execTimeout;
    }

    public void setExecTimeout(Integer execTimeout) {
        this.execTimeout = execTimeout;
    }

    public Byte getExecFailRetryCount() {
        return execFailRetryCount;
    }

    public void setExecFailRetryCount(Byte execFailRetryCount) {
        this.execFailRetryCount = execFailRetryCount;
    }

    public Byte getTriggerStatus() {
        return triggerStatus;
    }

    public void setTriggerStatus(Byte triggerStatus) {
        this.triggerStatus = triggerStatus;
    }

    public Long getTriggerLastTime() {
        return triggerLastTime;
    }

    public void setTriggerLastTime(Long triggerLastTime) {
        this.triggerLastTime = triggerLastTime;
    }

    public Long getTriggerNextTime() {
        return triggerNextTime;
    }

    public void setTriggerNextTime(Long triggerNextTime) {
        this.triggerNextTime = triggerNextTime;
    }
}