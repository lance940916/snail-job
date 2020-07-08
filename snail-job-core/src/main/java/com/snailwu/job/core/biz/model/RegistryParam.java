package com.snailwu.job.core.biz.model;

/**
 * @author 吴庆龙
 * @date 2020/5/25 2:41 下午
 */
public class RegistryParam {

    // 执行器组的
    private String groupUuid;

    // 注册节点的地址
    private String executorAddress;

    public RegistryParam() {
    }

    public RegistryParam(String groupUuid, String executorAddress) {
        this.groupUuid = groupUuid;
        this.executorAddress = executorAddress;
    }

    public String getGroupUuid() {
        return groupUuid;
    }

    public void setGroupUuid(String groupUuid) {
        this.groupUuid = groupUuid;
    }

    public String getExecutorAddress() {
        return executorAddress;
    }

    public void setExecutorAddress(String executorAddress) {
        this.executorAddress = executorAddress;
    }
}
