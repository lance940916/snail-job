package com.snailwu.job.admin.controller.request;

/**
 * @author 吴庆龙
 * @date 2020/8/10 8:05 上午
 */
public class JobInfoSearchRequest extends BasePageRequest {

    private String groupName;
    private String name;
    private String author;
    private Byte triggerStatus;

    public String getGroupName() {
        if (groupName == null || groupName.length() == 0) {
            return null;
        }
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getName() {
        if (name == null || name.length() == 0) {
            return null;
        }
        return "%" + name + "%";
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        if (author == null || author.length() == 0) {
            return null;
        }
        return "%" + author + "%";
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
