package com.snailwu.job.core.thread;

import com.snailwu.job.core.biz.model.RegistryParam;
import com.snailwu.job.core.biz.model.ResultT;
import com.snailwu.job.core.enums.RegistryConfig;
import com.snailwu.job.core.executor.SnailJobExecutor;
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
     * 注册器线程
     */
    private static Thread registryThread;

    /**
     * 注册线程停止标志
     */
    private static volatile boolean stopFlag = false;

    /**
     * 启动注册线程
     *
     * @param groupName 执行器的唯一标识
     * @param address 本机的外网地址 http://ip:port
     */
    public static void start(final String groupName, final String address) {
        if (groupName == null || groupName.trim().length() == 0) {
            LOGGER.warn("[SnailJob]-注册失败.没有配置groupName");
            return;
        }
        if (SnailJobExecutor.getAdminBiz() == null) {
            LOGGER.warn("[SnailJob]-注册失败.未配置调度中心地址");
            return;
        }

        // 不断的间隔一定时间进行注册
        registryThread = new Thread(() -> {
            while (!stopFlag) {
                // 注册
                try {
                    RegistryParam registryParam = new RegistryParam(groupName, address);
                    ResultT<String> result = SnailJobExecutor.getAdminBiz().registry(registryParam);
                    if (ResultT.SUCCESS_CODE != result.getCode()) {
                        LOGGER.error("[SnailJob]-在调度中心注册失败.原因:{}", result.getMsg());
                    }
                } catch (Exception e) {
                    if (!stopFlag) {
                        LOGGER.error("[SnailJob]-在调度中心注册异常.原因:{}", e.getMessage());
                    }
                }

                // 休眠
                try {
                    if (!stopFlag) {
                        TimeUnit.SECONDS.sleep(RegistryConfig.BEAT_TIME);
                    }
                } catch (InterruptedException e) {
                    // 正常停止的中断忽略
                    if (!stopFlag) {
                        LOGGER.error("[SnailJob]-休眠被中断.原因:{}", e.getMessage());
                    }
                }
            }

            // 移除节点。线程被停止后通知调度中心进行节点的移除
            try {
                RegistryParam registryParam = new RegistryParam(groupName, address);
                ResultT<String> result = SnailJobExecutor.getAdminBiz().registryRemove(registryParam);
                if (ResultT.SUCCESS_CODE != result.getCode()) {
                    LOGGER.error("[SnailJob]-通知调度中心移除注册节点失败.原因:{}", result.getMsg());
                }
            } catch (Exception e) {
                if (!stopFlag) {
                    LOGGER.error("[SnailJob]-通知调度中心移除注册节点异常.原因:{}", e.getMessage());
                }
            }
        });
        registryThread.setDaemon(true);
        registryThread.setName("executor-registry-thread");
        registryThread.start();
    }

    /**
     * 停止注册线程
     */
    public static void stop() {
        stopFlag = true;
        registryThread.interrupt();
        try {
            registryThread.join();
        } catch (InterruptedException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

}
