package com.snailwu.job.admin.controller.request;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @author 吴庆龙
 * @date 2020/8/18 10:16 上午
 */
public class JobLogDeleteRequest {

    private String groupName;
    private Integer jobId;
    @NotNull(message = "开始时间为空")
    private Date beginDate;
    @NotNull(message = "结束时间为空")
    private Date endDate;

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

    public Date getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(Date beginDate) {
        this.beginDate = beginDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
}
