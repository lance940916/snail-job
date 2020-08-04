package com.snailwu.job.core.biz.model;

/**
 * @author 吴庆龙
 * @date 2020/5/25 2:41 下午
 */
public class RegistryParam {

    // 执行器组的
    private String groupName;

    // 注册节点的地址
    private String address;

    public RegistryParam() {
    }

    public RegistryParam(String groupName, String address) {
        this.groupName = groupName;
        this.address = address;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
