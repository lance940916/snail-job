package com.snailwu.job.admin.controller.request;

import java.util.Date;

/**
 * @author 吴庆龙
 * @date 2020/8/18 10:16 上午
 */
public class JobLogSearchRequest extends BasePageRequest {

    private String groupName;
    private Integer jobId;
    private Integer triggerCode;
    private Integer execCode;
    private Date triggerBeginDate;
    private Date triggerEndDate;

    public String getGroupName() {
        if (groupName == null || groupName.length() == 0) {
            return null;
        }
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public Integer getJobId() {
        return jobId;
    }

    public void setJobId(Integer jobId) {
        this.jobId = jobId;
    }

    public Integer getTriggerCode() {
        return triggerCode;
    }

    public void setTriggerCode(Integer triggerCode) {
        this.triggerCode = triggerCode;
    }

    public Integer getExecCode() {
        return execCode;
    }

    public void setExecCode(Integer execCode) {
        this.execCode = execCode;
    }

    public Date getTriggerBeginDate() {
        return triggerBeginDate;
    }

    public void setTriggerBeginDate(Date triggerBeginDate) {
        this.triggerBeginDate = triggerBeginDate;
    }

    public Date getTriggerEndDate() {
        return triggerEndDate;
    }

    public void setTriggerEndDate(Date triggerEndDate) {
        this.triggerEndDate = triggerEndDate;
    }
}
