package com.snailwu.job.core.executor;

import com.snailwu.job.core.biz.AdminBiz;
import com.snailwu.job.core.biz.client.AdminBizClient;
import com.snailwu.job.core.handler.IJobHandler;
import com.snailwu.job.core.server.EmbedServer;
import com.snailwu.job.core.thread.CallbackThread;
import com.snailwu.job.core.thread.JobThread;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 执行器配置
 *
 * @author 吴庆龙
 * @date 2020/5/25 2:32 下午
 */
public abstract class JobExecutor {
    protected static final Logger LOGGER = LoggerFactory.getLogger(JobExecutor.class);

    // ----------------------------------------- 客户端配置-开始

    /**
     * 调度中心的地址
     * 具体到应用目录, 如: http://localhost:8080/job-admin
     */
    private String adminAddress;

    /**
     * 本机的外网 IP 地址
     */
    private String ip;

    /**
     * 本机与调度中心通讯的端口
     * 默认 7479
     */
    private int port = 7479;

    /**
     * 应用名称
     */
    private String appName;

    /**
     * 请求Token
     */
    private String accessToken;

    public void setAdminAddress(String adminAddress) {
        this.adminAddress = adminAddress;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    // ----------------------------------------- 客户端配置-结束

    /**
     * 调度中心通信的Client
     */
    private static AdminBiz adminBiz;

    public static AdminBiz getAdminBiz() {
        return adminBiz;
    }

    /**
     * 启动任务节点
     */
    public void start() {
        if (adminAddress == null || adminAddress.trim().length() == 0) {
            LOGGER.warn("没有配置调度中心地址，无法执行任务调度。");
            return;
        }

        // 实例化 AdminClient
        adminBiz = new AdminBizClient(adminAddress.trim(), accessToken);

        // 启动回调任务执行结果线程
        CallbackThread.start();

        // 启动 Netty 服务，并将节点注册到调度中心
        startEmbedServer();
    }

    /**
     * 停止任务节点
     */
    public void stop() {
        CallbackThread.stop();

        stopEmbedServer();
    }

    // -------------------------------- 启动停止 Netty

    /**
     * 执行器服务端，接收任务调度请求
     */
    private EmbedServer embedServer;

    /**
     * 启动执行器的内嵌服务端
     */
    private void startEmbedServer() {
        // 本地地址，注册到调度中心中
        String address = "http://" + ip + ":" + port;

        // 启动 Netty Server，与 Admin 进行通信
        embedServer = new EmbedServer();
        embedServer.start(port, address, appName, accessToken);
    }

    /**
     * 停止执行器的服务端
     */
    private void stopEmbedServer() {
        embedServer.stop();
    }

    // -------------------------------- JobHandler

    /**
     * key: 任务的名称
     * value: 任务的具体执行Method
     */
    private static final ConcurrentHashMap<String, IJobHandler> JOB_HANDLER_REPOSITORY = new ConcurrentHashMap<>();

    /**
     * 注册 JobHandler
     */
    public static void registryJobHandler(String name, IJobHandler jobHandler) {
        JOB_HANDLER_REPOSITORY.put(name, jobHandler);
        LOGGER.info("注册JobHandler成功. jobName:{}", name);
    }

    /**
     * 获取 JobHandler
     * 不存在则返回 null
     */
    public static IJobHandler loadJobHandler(String name) {
        return JOB_HANDLER_REPOSITORY.get(name);
    }

    // -------------------------------- JobThread

    /**
     * key: 任务的ID
     * value: 任务对应的执行线程
     */
    private static final ConcurrentHashMap<Integer, JobThread> JOB_THREAD_REPOSITORY = new ConcurrentHashMap<>();

    /**
     * 注册新的 jobThread，如果有相同的 jobId 就进行替换，并将之前的 jobId 对应的线程停止
     */
    public static JobThread registryAndStartJobThread(int jobId, IJobHandler handler) {
        JobThread jobThread = new JobThread(jobId, handler);
        jobThread.setName("jobThread-" + jobId);
        jobThread.start();
        LOGGER.info("创建并启动JobThread成功。jobId：{}", jobId);
        return jobThread;
    }

    /**
     * 移除并停止 JobThread
     */
    public static void removeAndStopJobThread(int jobId, String removeOldReason) {
        JobThread jobThread = JOB_THREAD_REPOSITORY.remove(jobId);
        stopJobThread(jobId, removeOldReason, jobThread);
    }

    /**
     * 停止 JobThread
     *
     * @param jobId           任务ID
     * @param removeOldReason 停止原因
     * @param jobThread       Thread
     */
    private static void stopJobThread(int jobId, String removeOldReason, JobThread jobThread) {
        if (jobThread == null) {
            return;
        }
        try {
            jobThread.toStop(removeOldReason);
            jobThread.interrupt();
            jobThread.join();
            LOGGER.info("停止JobThread成功。jobId：{}, 原因：{}", jobId, removeOldReason);
        } catch (InterruptedException e) {
            LOGGER.error("停止线程 {} 异常", jobThread.getName(), e);
        }
    }


    /**
     * 加载 JobThread
     */
    public static JobThread loadJobThread(int jobId) {
        return JOB_THREAD_REPOSITORY.get(jobId);
    }

}
