package com.snailwu.job.core.thread;

import com.snailwu.job.core.biz.model.RegistryParam;
import com.snailwu.job.core.biz.model.ResultT;
import com.snailwu.job.core.enums.RegistryConfig;
import com.snailwu.job.core.executor.SnailJobExecutor;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * 定时向调度中心注册执行器节点
 *
 * @author 吴庆龙
 * @date 2020/5/25 4:00 下午
 */
public class ExecutorRegistryThread {
    public static final Logger LOGGER = LoggerFactory.getLogger(ExecutorRegistryThread.class);

    /**
     * 私有化构造方法
     */
    private ExecutorRegistryThread() {
    }

    /**
     * 注册器线程
     */
    private static Thread nodeRegistryThread;

    /**
     * 注册线程停止标志
     */
    private static volatile boolean stopFlag = false;

    /**
     * 启动注册线程
     *
     * @param executorName 执行器的唯一标识
     * @param nodeAddress 本机的外网地址 http://ip:port
     */
    public static void start(final String executorName, final String nodeAddress) {
        if (StringUtils.isEmpty(executorName)) {
            LOGGER.warn("[SnailJob]-注册失败.没有配置 executorName");
            return;
        }
        if (SnailJobExecutor.getAdminBiz() == null) {
            LOGGER.warn("[SnailJob]-注册失败.未配置调度中心地址");
            return;
        }

        // 不断的间隔一定时间进行注册
        nodeRegistryThread = new Thread(() -> {
            while (!stopFlag) {
                try {
                    RegistryParam registryParam = new RegistryParam(executorName, nodeAddress);
                    ResultT<String> registryResult = SnailJobExecutor.getAdminBiz().registry(registryParam);
                    if (registryResult != null && ResultT.SUCCESS_CODE == registryResult.getCode()) {
                        LOGGER.info("[SnailJob]-在调度中心注册成功");
                    } else {
                        LOGGER.error("[SnailJob]-在调度中心注册失败");
                    }
                } catch (Exception e) {
                    if (!stopFlag) {
                        LOGGER.error("[SnailJob]-在调度中心注册异常", e);
                    }
                }

                // 线程没有标记为停止，则进行休眠
                try {
                    if (!stopFlag) {
                        TimeUnit.SECONDS.sleep(RegistryConfig.BEAT_TIMEOUT);
                    }
                } catch (InterruptedException e) {
                    if (!stopFlag) {
                        LOGGER.error("[SnailJob]-休眠被中断，且 toStop 为 false. errMsg:{}", e.getMessage());
                    }
                }
            }

            // 移除节点。线程被停止后（toStop 为 true）通知调度中心进行节点的移除
            try {
                RegistryParam registryParam = new RegistryParam(executorName, nodeAddress);
                ResultT<String> registryResult = SnailJobExecutor.getAdminBiz().registryRemove(registryParam);
                if (registryResult != null && ResultT.SUCCESS_CODE == registryResult.getCode()) {
                    LOGGER.info("[SnailJob]-通知调度中心移除注册节点成功");
                } else {
                    LOGGER.info("[SnailJob]-通知调度中心移除注册节点失败");
                }
            } catch (Exception e) {
                if (!stopFlag) {
                    LOGGER.error("[SnailJob]-通知调度中心移除注册节点异常", e);
                }
            }
        });
        nodeRegistryThread.setDaemon(true);
        nodeRegistryThread.setName("executorRegistryThread");
        nodeRegistryThread.start();
        LOGGER.info("[SnailJob]-注册节点守护线程 启动成功");
    }

    public static void stop() {
        stopFlag = true;
        nodeRegistryThread.interrupt();

        try {
            nodeRegistryThread.join();
        } catch (InterruptedException e) {
            LOGGER.error(e.getMessage(), e);
        }
        LOGGER.info("[SnailJob]-注册节点守护线程 停止成功");
    }

}
