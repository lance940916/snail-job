package com.snailwu.job.core.executor;

import com.snailwu.job.core.biz.AdminBiz;
import com.snailwu.job.core.biz.client.AdminBizClient;
import com.snailwu.job.core.executor.model.SnailJobConfig;
import com.snailwu.job.core.handler.IJobHandler;
import com.snailwu.job.core.server.EmbedServer;
import com.snailwu.job.core.thread.JobThread;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author 吴庆龙
 * @date 2020/5/25 2:32 下午
 */
public class SnailJobExecutor {
    public static final Logger log = LoggerFactory.getLogger(SnailJobExecutor.class);

    /**
     * 调度中心实例
     */
    private static AdminBiz adminBiz;

    /**
     * 执行器服务端，接收 RPC 请求
     */
    private EmbedServer embedServer;

    /**
     * JobHandler Repository
     */
    private static ConcurrentHashMap<String, IJobHandler> jobHandlerRepository = new ConcurrentHashMap<>();

    /**
     * JobThread Repository
     */
    private static ConcurrentHashMap<Integer, JobThread> jobThreadRepository = new ConcurrentHashMap<>();

    /**
     * SnailJob 的配置参数
     */
    private SnailJobConfig snailJobConfig;

    public SnailJobExecutor(SnailJobConfig snailJobConfig) {
        this.snailJobConfig = snailJobConfig;
    }

    /**
     * 启动、初始化
     */
    public void start() {
        // TODO 日志模块


        // 实例化 AdminClient 实例
        initAdminBiz(snailJobConfig.getAdminAddress());

        // 启动 RPC 服务，并注册节点到调度中心
        startEmbedServer();
    }

    /**
     * 停止
     */
    public void stop() {
        stopEmbedServer();

    }

    /**
     * 实例化调度中心Client，用来发起请求给调度中心
     */
    private void initAdminBiz(String adminAddress) {
        if (adminAddress == null || adminAddress.trim().length() == 0) {
            log.warn("没有配置调度中心地址");
            return;
        }
        // 实例化调度中心
        adminBiz = new AdminBizClient(adminAddress.trim());
    }

    public static AdminBiz getAdminBiz() {
        return adminBiz;
    }

    /**
     * 启动执行器的服务端
     */
    private void startEmbedServer() {
        // 确定 address
        String address = snailJobConfig.getAddress();
        String ip = snailJobConfig.getIp();
        int port = snailJobConfig.getPort();
        if (address == null || address.trim().length() == 0) {
            String ipPortAddress = ip.concat(":").concat(String.valueOf(port));
            address = "http://" + ipPortAddress;
        }

        // 启动
        embedServer = new EmbedServer();
        embedServer.start(port, address, snailJobConfig.getAppName());
    }

    /**
     * 停止执行器的服务端
     */
    private void stopEmbedServer() {
        try {
            embedServer.stop();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    /**
     * 添加 JobHandler
     */
    public static IJobHandler registryJobHandler(String name, IJobHandler jobHandler) {
        log.info("注册 JobHandler 成功 name:{} handler:{}", name, jobHandler);
        return jobHandlerRepository.put(name, jobHandler);
    }

    /**
     * 获取 JobHandler
     */
    public static IJobHandler loadJobHandler(String name) {
        return jobHandlerRepository.get(name);
    }

    /**
     * 注册 JobThread
     */
    public static JobThread registryJobThread(int jobId, IJobHandler handler, String removeOldReason) {
        JobThread jobThread = new JobThread(jobId, handler);
        jobThread.start();
        log.info("snail-job registry jobThread success. jobId:{}, handler:{}", jobId, handler);

        JobThread oldJobThread = jobThreadRepository.put(jobId, jobThread);
        if (oldJobThread != null) {
            oldJobThread.toStop(removeOldReason);
            oldJobThread.interrupt();
        }
        return jobThread;
    }

    /**
     * 移除 JobThread
     */
    public static JobThread removeJobThread(int jobId, String removeOldReason) {
        JobThread jobThread = jobThreadRepository.remove(jobId);
        if (jobThread != null) {
            jobThread.toStop(removeOldReason);
            jobThread.interrupt();
            return jobThread;
        }
        return null;
    }

    /**
     * 加载 JobThread
     */
    public static JobThread loadJobThread(int jobId) {
        return jobThreadRepository.get(jobId);
    }

}
