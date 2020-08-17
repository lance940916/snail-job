package com.snailwu.job.admin.controller.request;

import com.snailwu.job.admin.core.model.JobInfo;

import javax.validation.constraints.NotEmpty;
import java.util.Date;

public class JobInfoEditRequest {

    private Integer id;
    @NotEmpty(message = "名称不能为空")
    private String name;
    @NotEmpty(message = "分组不能为空")
    private String groupName;
    @NotEmpty(message = "Cron表达式不能为空")
    private String cron;
    private Date updateTime;
    @NotEmpty(message = "负责人不能为空")
    private String author;
    private String alarmEmail;
    @NotEmpty(message = "路由策略不能为空")
    private String executorRouteStrategy;
    @NotEmpty(message = "执行方法不能为空")
    private String executorHandler;
    private String executorParam;
    private Integer executorTimeout;
    private Byte executorFailRetryCount;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getCron() {
        return cron;
    }

    public void setCron(String cron) {
        this.cron = cron;
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

    public String getExecutorRouteStrategy() {
        return executorRouteStrategy;
    }

    public void setExecutorRouteStrategy(String executorRouteStrategy) {
        this.executorRouteStrategy = executorRouteStrategy;
    }

    public String getExecutorHandler() {
        return executorHandler;
    }

    public void setExecutorHandler(String executorHandler) {
        this.executorHandler = executorHandler;
    }

    public String getExecutorParam() {
        return executorParam;
    }

    public void setExecutorParam(String executorParam) {
        this.executorParam = executorParam;
    }

    public Integer getExecutorTimeout() {
        return executorTimeout;
    }

    public void setExecutorTimeout(Integer executorTimeout) {
        this.executorTimeout = executorTimeout;
    }

    public Byte getExecutorFailRetryCount() {
        return executorFailRetryCount;
    }

    public void setExecutorFailRetryCount(Byte executorFailRetryCount) {
        this.executorFailRetryCount = executorFailRetryCount;
    }

    public JobInfo convertToJobInfo() {
        JobInfo jobInfo = new JobInfo();
        jobInfo.setId(this.id);
        jobInfo.setName(this.name);
        jobInfo.setGroupName(this.groupName);
        jobInfo.setCron(this.cron);
        jobInfo.setUpdateTime(this.updateTime);
        jobInfo.setAuthor(this.author);
        jobInfo.setAlarmEmail(this.alarmEmail);
        jobInfo.setExecutorRouteStrategy(this.executorRouteStrategy);
        jobInfo.setExecutorHandler(this.executorHandler);
        jobInfo.setExecutorParam(this.executorParam);
        jobInfo.setExecutorTimeout(this.executorTimeout);
        jobInfo.setExecutorFailRetryCount(this.executorFailRetryCount);
        return jobInfo;
    }

}