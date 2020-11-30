package com.snailwu.job.core.node;

/**
 * @author 吴庆龙
 * @date 2020/11/27 上午11:11
 */
public class SnailJobNodeProperties {

    /**
     * 调度中心的地址
     * 具体到应用目录, 如: http://localhost:8080/job-admin
     */
    private String adminAddress;

    /**
     * 本机的外网 IP 地址
     * 调度中心通过 ip + port 来执行调度
     */
    private String hostIp;

    /**
     * 本机与调度中心通讯的端口
     * 默认 7479
     */
    private int hostPort = 7479;

    /**
     * 应用名称
     */
    private String appName;

    /**
     * 调度中心与节点之间请求的加密
     */
    private String accessToken;

    public String getAdminAddress() {
        return adminAddress;
    }

    public void setAdminAddress(String adminAddress) {
        this.adminAddress = adminAddress;
    }

    public String getHostIp() {
        return hostIp;
    }

    public void setHostIp(String hostIp) {
        this.hostIp = hostIp;
    }

    public int getHostPort() {
        return hostPort;
    }

    public void setHostPort(int hostPort) {
        this.hostPort = hostPort;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

}
