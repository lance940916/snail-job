package com.snailwu.job.core.executor.model;

/**
 * 执行器的配置
 *
 * @author 吴庆龙
 * @date 2020/5/26 10:48 上午
 */
public class ExecutorConfiguration {

    /**
     * 调度中心的地址
     * 具体到 Context 根目录, 如: http://localhost:8080/job-admin
     */
    private String adminAddress;

    /**
     * 本机的外网 IP 地址
     */
    private String ip;

    /**
     * 本机与调度中心通讯的端口
     */
    private int port = 9999;

    /**
     * 执行器组 Name
     */
    private String groupName;

    public String getAdminAddress() {
        return adminAddress;
    }

    public void setAdminAddress(String adminAddress) {
        this.adminAddress = adminAddress;
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

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
}
