package com.snailwu.job.admin.request;

/**
 * @author 吴庆龙
 * @date 2020/8/10 8:05 上午
 */
public class JobInfoSearchRequest extends BasePageRequest {

    private String groupName;
    private String author;
    private Byte triggerStatus;

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Byte getTriggerStatus() {
        return triggerStatus;
    }

    public void setTriggerStatus(Byte triggerStatus) {
        this.triggerStatus = triggerStatus;
    }

}
