package com.snailwu.job.core.biz.model;

/**
 * @author 吴庆龙
 * @date 2020/5/25 2:41 下午
 */
public class RegistryParam {

    // 执行器组的
    private String groupName;

    // 注册节点的地址
    private String executorAddress;

    public RegistryParam() {
    }

    public RegistryParam(String groupName, String executorAddress) {
        this.groupName = groupName;
        this.executorAddress = executorAddress;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getExecutorAddress() {
        return executorAddress;
    }

    public void setExecutorAddress(String executorAddress) {
        this.executorAddress = executorAddress;
    }
}
