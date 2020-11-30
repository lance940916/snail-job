package com.snailwu.job.core.executor;

import com.snailwu.job.core.biz.AdminBiz;
import com.snailwu.job.core.biz.client.AdminBizClient;
import com.snailwu.job.core.handler.IJobHandler;
import com.snailwu.job.core.server.EmbedServer;
import com.snailwu.job.core.thread.JobThread;
import com.snailwu.job.core.thread.ResultCallbackThread;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 执行器配置
 *
 * 需要扫描标有 @SnailJob 注解的方法注册到 JOB_HANDLER_REPOSITORY 中
 *
 * @author 吴庆龙
 * @date 2020/5/25 2:32 下午
 */
public abstract class SnailJobExecutor {
    protected static final Logger LOGGER = LoggerFactory.getLogger(SnailJobExecutor.class);

    /**
     * 配置参数
     */
    private final SnailJobNodeProperties configuration;

    /**
     * 调度中心通信的Client
     */
    private static AdminBiz adminBiz;

    /**
     * 执行器服务端，接收任务调度请求
     */
    private EmbedServer embedServer;

    /**
     * 存储 JobHandler
     */
    private static final ConcurrentHashMap<String, IJobHandler> JOB_HANDLER_REPOSITORY = new ConcurrentHashMap<>();

    /**
     * 存储 JobThread
     */
    private static final ConcurrentHashMap<Integer, JobThread> JOB_THREAD_REPOSITORY = new ConcurrentHashMap<>();

    /**
     * 注入配置
     */
    public SnailJobExecutor(SnailJobNodeProperties configuration) {
        this.configuration = configuration;
    }

    /**
     * 启动任务节点
     */
    public void start() {
        // 实例化 AdminClient 实例
        initAdminBizClient(configuration.getAdminAddress(), configuration.getAccessToken());

        // 启动回调任务执行结果线程
        ResultCallbackThread.start();

        // 启动 Netty 服务，并将节点注册到调度中心
        startEmbedServer();
    }

    /**
     * 停止任务节点
     */
    public void stop() {
        ResultCallbackThread.stop();

        stopEmbedServer();
    }

    /**
     * 实例化调度中心Client，用来发起请求给调度中心
     */
    private void initAdminBizClient(String adminAddress, String accessToken) {
        if (adminAddress == null || adminAddress.trim().length() == 0) {
            LOGGER.warn("[SnailJob]-没有配置调度中心地址，无法连接到调度中心");
            return;
        }

        // 实例化调度中心
        adminBiz = new AdminBizClient(adminAddress.trim(), accessToken);
    }

    public static AdminBiz getAdminBiz() {
        return adminBiz;
    }

    // -------------------------------- 启动停止 Netty

    /**
     * 启动执行器的内嵌服务端
     */
    private void startEmbedServer() {
        String hostIp = configuration.getHostIp();
        int hostPort = configuration.getHostPort();
        String appName = configuration.getAppName();
        String accessToken = configuration.getAccessToken();

        // 组装本地地址，注册到调度中心中
        String hostAddress = "http://" + hostIp + ":" + hostPort;

        // 启动 Netty Server，与 Admin 进行通信
        embedServer = new EmbedServer();
        embedServer.start(hostPort, hostAddress, appName, accessToken);
    }

    /**
     * 停止执行器的服务端
     */
    private void stopEmbedServer() {
        try {
            embedServer.stop();
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    // -------------------------------- JobHandler

    /**
     * 注册 JobHandler
     */
    public static void registryJobHandler(String name, IJobHandler jobHandler) {
        JOB_HANDLER_REPOSITORY.put(name, jobHandler);
        LOGGER.info("[SnailJob]-注册JobHandler成功. name:{}, handler:{}", name, jobHandler);
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
     * 注册新的 jobThread，如果有相同的 jobId 就进行替换，并将之前的 jobId 对应的线程停止
     */
    public static JobThread registryJobThread(int jobId, IJobHandler handler, String removeOldReason) {
        JobThread jobThread = new JobThread(jobId, handler);
        jobThread.setName("job-id-" + jobId);
        jobThread.start();
        LOGGER.info("[SnailJob]-注册JobThread成功.jobId:{},handler:{}", jobId, handler);

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
    public static void removeJobThread(int jobId, String removeOldReason) {
        JobThread jobThread = JOB_THREAD_REPOSITORY.remove(jobId);
        if (jobThread != null) {
            jobThread.toStop(removeOldReason);
            jobThread.interrupt();
            try {
                jobThread.join();
            } catch (InterruptedException e) {
                LOGGER.error(e.getMessage(), e);
            }
        }
    }

    /**
     * 加载 JobThread
     */
    public static JobThread loadJobThread(int jobId) {
        return JOB_THREAD_REPOSITORY.get(jobId);
    }

}
