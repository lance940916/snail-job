package com.snailwu.job.admin.model;

import javax.annotation.Generated;
import java.util.Date;

public class JobInfo {
    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private Integer id;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private String name;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private String appName;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private String cron;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private Date createTime;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private Date updateTime;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private String author;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private String alarmEmail;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private String execRouteStrategy;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private String execHandler;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private String execParam;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private Integer execTimeout;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private Byte execFailRetryCount;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private Byte triggerStatus;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private Long triggerLastTime;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private Long triggerNextTime;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public Integer getId() {
        return id;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setId(Integer id) {
        this.id = id;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public String getName() {
        return name;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setName(String name) {
        this.name = name;
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
    public String getCron() {
        return cron;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setCron(String cron) {
        this.cron = cron;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public Date getCreateTime() {
        return createTime;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public Date getUpdateTime() {
        return updateTime;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public String getAuthor() {
        return author;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setAuthor(String author) {
        this.author = author;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public String getAlarmEmail() {
        return alarmEmail;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setAlarmEmail(String alarmEmail) {
        this.alarmEmail = alarmEmail;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public String getExecRouteStrategy() {
        return execRouteStrategy;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setExecRouteStrategy(String execRouteStrategy) {
        this.execRouteStrategy = execRouteStrategy;
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
    public Integer getExecTimeout() {
        return execTimeout;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setExecTimeout(Integer execTimeout) {
        this.execTimeout = execTimeout;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public Byte getExecFailRetryCount() {
        return execFailRetryCount;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setExecFailRetryCount(Byte execFailRetryCount) {
        this.execFailRetryCount = execFailRetryCount;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public Byte getTriggerStatus() {
        return triggerStatus;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setTriggerStatus(Byte triggerStatus) {
        this.triggerStatus = triggerStatus;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public Long getTriggerLastTime() {
        return triggerLastTime;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setTriggerLastTime(Long triggerLastTime) {
        this.triggerLastTime = triggerLastTime;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public Long getTriggerNextTime() {
        return triggerNextTime;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setTriggerNextTime(Long triggerNextTime) {
        this.triggerNextTime = triggerNextTime;
    }
}