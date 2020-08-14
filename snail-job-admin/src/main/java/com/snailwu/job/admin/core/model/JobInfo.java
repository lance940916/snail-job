package com.snailwu.job.admin.core.model;

import javax.annotation.Generated;
import javax.validation.constraints.NotEmpty;
import java.util.Date;

public class JobInfo {
    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private Integer id;

    @NotEmpty(message = "分组不能为空")
    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private String groupName;

    @NotEmpty(message = "Cron表达式不能为空")
    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private String cron;

    @NotEmpty(message = "描述不能为空")
    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private String description;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private Date createTime;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private Date updateTime;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private String author;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private String alarmEmail;

    @NotEmpty(message = "路由策略不能为空")
    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private String executorRouteStrategy;

    @NotEmpty(message = "JobHandler不能为空")
    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private String executorHandler;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private String executorParam;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private Integer executorTimeout;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private Byte executorFailRetryCount;

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
    public String getGroupName() {
        return groupName;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setGroupName(String groupName) {
        this.groupName = groupName;
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
    public String getDescription() {
        return description;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setDescription(String description) {
        this.description = description;
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
    public String getExecutorRouteStrategy() {
        return executorRouteStrategy;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setExecutorRouteStrategy(String executorRouteStrategy) {
        this.executorRouteStrategy = executorRouteStrategy;
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
    public Integer getExecutorTimeout() {
        return executorTimeout;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setExecutorTimeout(Integer executorTimeout) {
        this.executorTimeout = executorTimeout;
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