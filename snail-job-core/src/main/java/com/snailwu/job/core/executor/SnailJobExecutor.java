package com.snailwu.job.core.executor;

import com.snailwu.job.core.biz.AdminBiz;
import com.snailwu.job.core.biz.client.AdminBizClient;
import com.snailwu.job.core.executor.model.SnailJobConfiguration;
import com.snailwu.job.core.handler.IJobHandler;
import com.snailwu.job.core.server.EmbedServer;
import com.snailwu.job.core.thread.JobThread;
import com.snailwu.job.core.thread.TriggerCallbackThread;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 执行器配置
 *
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
    private static final ConcurrentHashMap<String, IJobHandler> JOB_HANDLER_REPOSITORY = new ConcurrentHashMap<>();

    /**
     * JobThread Repository
     */
    private static final ConcurrentHashMap<Integer, JobThread> JOB_THREAD_REPOSITORY = new ConcurrentHashMap<>();

    /**
     * SnailJob 的配置参数
     */
    private final SnailJobConfiguration snailJobConfiguration;

    public SnailJobExecutor(SnailJobConfiguration snailJobConfiguration) {
        this.snailJobConfiguration = snailJobConfiguration;
    }

    // -------------------------------- 启动停止 snail-job

    /**
     * 启动
     */
    public void start() {
        // 实例化 AdminClient 实例
        initAdminBiz(snailJobConfiguration.getAdminAddress());

        // 启动回调任务执行结果线程
        TriggerCallbackThread.getInstance().start();

        // 启动 Netty 服务，并将节点注册到调度中心
        startEmbedServer();
    }

    /**
     * 停止
     */
    public void stop() {
        TriggerCallbackThread.getInstance().stop();
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

    // -------------------------------- 启动停止 Netty

    /**
     * 启动执行器的服务端
     */
    private void startEmbedServer() {
        // 确定 address
        String address = snailJobConfiguration.getAddress();
        String ip = snailJobConfiguration.getIp();
        int port = snailJobConfiguration.getPort();
        if (address == null || address.trim().length() == 0) {
            String ipPortAddress = ip.concat(":").concat(String.valueOf(port));
            address = "http://" + ipPortAddress;
        }

        // 启动
        embedServer = new EmbedServer();
        embedServer.start(port, address, snailJobConfiguration.getAppName());
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

    // -------------------------------- JobHandler

    /**
     * 添加 JobHandler
     */
    public static IJobHandler registryJobHandler(String name, IJobHandler jobHandler) {
        log.info("注册 JobHandler 成功 name:{} handler:{}", name, jobHandler);
        return JOB_HANDLER_REPOSITORY.put(name, jobHandler);
    }

    /**
     * 获取 JobHandler
     */
    public static IJobHandler loadJobHandler(String name) {
        return JOB_HANDLER_REPOSITORY.get(name);
    }

    // -------------------------------- JobThread

    /**
     * 注册新的 jobThread，如果有相同的 jobId 就进行替换，并将之前的 jobId 对应的线程停止
     */
    public static JobThread registryJobThread(int jobId, IJobHandler handler, String removeOldReason) {
        JobThread jobThread = new JobThread(jobId, handler);
        jobThread.start();
        log.info("snail-job registry jobThread success. jobId:{}, handler:{}", jobId, handler);

        JobThread oldJobThread = JOB_THREAD_REPOSITORY.put(jobId, jobThread);
        if (oldJobThread != null) {
            oldJobThread.toStop(removeOldReason);
            oldJobThread.interrupt();
        }
        return jobThread;
    }

    /**
     * 移除并停止 JobThread
     */
    public static JobThread removeJobThread(int jobId, String removeOldReason) {
        JobThread jobThread = JOB_THREAD_REPOSITORY.remove(jobId);
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
        return JOB_THREAD_REPOSITORY.get(jobId);
    }

}
