package com.snailwu.job.core.biz.model;

/**
 * 节点参数
 *
 * @author 吴庆龙
 * @date 2020/5/25 2:41 下午
 */
public class RegistryParam {

    /**
     * 应用名称
     */
    private String appName;

    /**
     * 执行器的地址
     */
    private String address;

    public RegistryParam() {
    }

    public RegistryParam(String appName, String address) {
        this.appName = appName;
        this.address = address;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
