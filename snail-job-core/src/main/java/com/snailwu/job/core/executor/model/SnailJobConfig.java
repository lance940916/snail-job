package com.snailwu.job.core.executor.model;

/**
 * @author 吴庆龙
 * @date 2020/5/26 10:48 上午
 */
public class SnailJobConfig {

    /**
     * 调度中心的地址
     */
    private String adminAddress;

    /**
     * 本机的 ip 地址
     */
    private String ip;

    /**
     * 本机的端口
     */
    private int port;

    /**
     * 本机的地址，优先使用这个字段，为 null 则使用 ip + port
     */
    private String address;

    /**
     * 执行器的 appName
     */
    private String appName;


    public String getAdminAddress() {
        return adminAddress;
    }

    public void setAdminAddress(String adminAddress) {
        this.adminAddress = adminAddress;
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

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

}
