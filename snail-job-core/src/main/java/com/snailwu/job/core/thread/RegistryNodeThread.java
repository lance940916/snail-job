package com.snailwu.job.core.thread;

import com.snailwu.job.core.biz.model.RegistryParam;
import com.snailwu.job.core.biz.model.ResultT;
import com.snailwu.job.core.constants.RegistryConstant;
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
public class RegistryNodeThread {
    private static final Logger LOGGER = LoggerFactory.getLogger(RegistryNodeThread.class);

    /**
     * 注册器线程
     */
    private static Thread thread;

    /**
     * 注册线程停止标志
     */
    private static volatile boolean running = true;

    /**
     * 启动注册线程
     *
     * @param appName 执行器的唯一标识
     * @param address 本机的外网地址 http://ip:port
     */
    public static void start(final String appName, final String address) {
        if (appName == null || appName.trim().length() == 0) {
            LOGGER.warn("没有配置appName。");
            return;
        }
        if (SnailJobExecutor.getAdminBiz() == null) {
            LOGGER.warn("未配置调度中心地址。");
            return;
        }

        thread = new Thread(() -> {
            while (running) {
                // 在调度中心注册节点
                RegistryParam registryParam = new RegistryParam(appName, address);
                try {
                    ResultT<String> result = SnailJobExecutor.getAdminBiz().registry(registryParam);
                    if (ResultT.SUCCESS_CODE != result.getCode()) {
                        LOGGER.error("在调度中心注册失败。原因：{}", result.getMsg());
                    }
                    LOGGER.info("在调度中心注册成功。本机地址：{}，注册应用：{}", address, appName);
                } catch (Exception e) {
                    if (running) {
                        LOGGER.error("在调度中心注册异常。原因：{}", e.getMessage());
                    }
                }

                // 休眠
                try {
                    if (running) {
                        TimeUnit.SECONDS.sleep(RegistryConstant.BEAT_TIME);
                    }
                } catch (InterruptedException e) {
                    if (running) {
                        LOGGER.warn("注册线程休眠被中断");
                    }
                }
            }

            // 移除节点。线程被停止后通知调度中心进行节点的移除
            RegistryParam registryParam = new RegistryParam(appName, address);
            try {
                ResultT<String> result = SnailJobExecutor.getAdminBiz().registryRemove(registryParam);
                if (ResultT.SUCCESS_CODE != result.getCode()) {
                    LOGGER.error("通知调度中心移除注册节点失败。原因：{}", result.getMsg());
                }
                LOGGER.info("通知调度中心移除注册节点成功。");
            } catch (Exception e) {
                if (running) {
                    LOGGER.error("通知调度中心移除注册节点异常。原因：{}", e.getMessage());
                }
            }
        });
        thread.setDaemon(true);
        thread.setName("registry-node-thread");
        thread.start();
    }

    /**
     * 停止注册线程
     */
    public static void stop() {
        running = false;
        thread.interrupt();
        try {
            // 主线程调用 thread.join() 等待 thread 线程执行完毕再往下执行
            thread.join();
        } catch (InterruptedException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

}
