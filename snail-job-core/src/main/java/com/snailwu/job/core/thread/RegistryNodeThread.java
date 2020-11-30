package com.snailwu.job.core.thread;

import com.snailwu.job.core.biz.AdminBiz;
import com.snailwu.job.core.biz.model.RegistryParam;
import com.snailwu.job.core.biz.model.ResultT;
import com.snailwu.job.core.constants.RegistryConstant;
import com.snailwu.job.core.node.SnailJobNode;
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
        if (SnailJobNode.getAdminBiz() == null) {
            return;
        }
        if (appName == null || appName.trim().length() == 0) {
            LOGGER.warn("没有配置appName。");
            return;
        }

        thread = new Thread(() -> {
            AdminBiz adminBiz = SnailJobNode.getAdminBiz();

            while (running) {
                // 在调度中心注册节点
                RegistryParam registryParam = new RegistryParam(appName, address);
                try {
                    ResultT<String> result = adminBiz.registryNode(registryParam);
                    if (ResultT.SUCCESS_CODE != result.getCode()) {
                        LOGGER.error("在调度中心注册失败。原因：{}", result.getMsg());
                    } else {
                        LOGGER.info("在调度中心注册成功。本机地址：{}，注册应用：{}", address, appName);
                    }
                } catch (Exception e) {
                    LOGGER.error("在调度中心注册异常。", e);
                }

                // 休眠
                if (running) {
                    try {
                        TimeUnit.SECONDS.sleep(RegistryConstant.BEAT_TIME);
                    } catch (Exception e) {
                        // ignore
                    }
                }
            }

            // 移除节点。线程被停止后通知调度中心进行节点的移除
            RegistryParam registryParam = new RegistryParam(appName, address);
            try {
                ResultT<String> result = adminBiz.removeNode(registryParam);
                if (ResultT.SUCCESS_CODE != result.getCode()) {
                    LOGGER.error("通知调度中心移除注册节点失败。原因：{}", result.getMsg());
                } else {
                    LOGGER.info("通知调度中心移除注册节点成功。");
                }
            } catch (Exception e) {
                LOGGER.error("通知调度中心移除注册节点异常。", e);
            }
        });
        thread.setDaemon(true);
        thread.setName("registry-thread");
        thread.start();
        LOGGER.info("注册线程-已启动。");
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
        LOGGER.info("注册线程-已停止。");
    }

}
