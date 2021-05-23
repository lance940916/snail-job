package com.snailwu.job.core.thread;

import com.snailwu.job.core.biz.AdminBiz;
import com.snailwu.job.core.biz.model.RegistryParam;
import com.snailwu.job.core.biz.model.ResultT;
import com.snailwu.job.core.constants.CoreConstant;
import com.snailwu.job.core.executor.JobExecutor;
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
     * @param appName 执行器所属应用
     * @param address 本机的外网地址 http://ip:port
     */
    public static void start(String appName, String address) {
        if (JobExecutor.getAdminBiz() == null) {
            return;
        }
        if (appName == null || appName.trim().length() == 0) {
            LOGGER.warn("appName属性未配置。");
            return;
        }

        thread = new Thread(() -> {
            AdminBiz adminBiz = JobExecutor.getAdminBiz();

            while (running) {
                // 在调度中心注册执行器
                try {
                    RegistryParam registryParam = new RegistryParam(appName, address);
                    ResultT<String> result = adminBiz.registry(registryParam);
                    if (ResultT.SUCCESS_CODE == result.getCode()) {
                        LOGGER.info("在调度中心注册成功。本机地址：{}，绑定应用：{}", address, appName);
                    } else {
                        LOGGER.error("在调度中心注册失败。原因：{}", result.getMsg());
                    }
                } catch (Exception e) {
                    LOGGER.error("在调度中心注册异常。", e);
                }

                // 休眠
                if (running) {
                    try {
                        TimeUnit.SECONDS.sleep(CoreConstant.BEAT_TIME);
                    } catch (Exception e) {
                        // ignore
                    }
                }
            }

            // 移除节点。线程被中断后通知调度中心进行节点的移除
            try {
                RegistryParam registryParam = new RegistryParam(appName, address);
                ResultT<String> result = adminBiz.remove(registryParam);
                if (ResultT.SUCCESS_CODE == result.getCode()) {
                    LOGGER.info("通知调度中心移除执行器成功。");
                } else {
                    LOGGER.error("通知调度中心移除执行器失败。原因：{}", result.getMsg());
                }
            } catch (Exception e) {
                LOGGER.error("通知调度中心移除执行器异常。", e);
            }
        });
        thread.setDaemon(true);
        thread.setName("registry-thread");
        thread.start();
        LOGGER.info("注册执行器线程-已启动。");
    }

    /**
     * 停止注册线程
     */
    public static void stop() {
        running = false;
        try {
            thread.interrupt();
            thread.join();
            LOGGER.info("注册执行器线程-已停止。");
        } catch (Exception e) {
            LOGGER.error("停止线程 {} 异常", thread.getName(), e);
        }
    }

}
