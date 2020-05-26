package com.snailwu.job.core.thread;

import com.snailwu.job.core.biz.AdminBiz;
import com.snailwu.job.core.biz.model.RegistryParam;
import com.snailwu.job.core.biz.model.ResultT;
import com.snailwu.job.core.enums.RegistryConfig;
import com.snailwu.job.core.executor.SnailJobExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

import static com.snailwu.job.core.enums.RegistryConfig.RegistryType.EXECUTOR;

/**
 * @author 吴庆龙
 * @date 2020/5/25 4:00 下午
 */
public class ExecutorRegistryThread {
    public static final Logger log = LoggerFactory.getLogger(ExecutorRegistryThread.class);

    private static ExecutorRegistryThread instance = new ExecutorRegistryThread();

    public static ExecutorRegistryThread getInstance() {
        return instance;
    }

    private Thread registryThread;
    private volatile boolean toStop = false;

    /**
     * 启动注册线程
     * @param appName 执行器的唯一标识
     * @param address 本机的外网地址 http://ip:port
     */
    public void start(final String appName, final String address) {
        if (appName == null || appName.trim().length() == 0) {
            log.warn("执行器注册失败, appName is null");
            return;
        }
        if (SnailJobExecutor.getAdminBiz() == null) {
            log.warn("执行器注册失败, 未配置调度中心地址");
            return;
        }

        registryThread = new Thread(() -> {
            // 一直进行注册
            while (!toStop) {
                try {
                    RegistryParam registryParam = new RegistryParam(EXECUTOR.name(), appName, address);
                    AdminBiz adminBiz = SnailJobExecutor.getAdminBiz();
                    ResultT<String> registryResult = adminBiz.registry(registryParam);
                    if (registryResult != null && ResultT.SUCCESS_CODE == registryResult.getCode()) {
                        log.info("注册到调度中心成功");
                    } else {
                        log.error("注册到调度中心失败");
                    }
                } catch (Exception e) {
                    if (!toStop) {
                        log.error(e.getMessage(), e);
                    }
                }

                // 休眠
                try {
                    if (!toStop) {
                        TimeUnit.SECONDS.sleep(RegistryConfig.BEAT_TIMEOUT);
                    }
                } catch (InterruptedException e) {
                    if (!toStop) {
                        log.error("执行器注册线程被中断了, errMsg:{}", e.getMessage());
                    }
                }
            }

            // 移除节点
            try {
                RegistryParam registryParam = new RegistryParam(EXECUTOR.name(), appName, address);
                AdminBiz adminBiz = SnailJobExecutor.getAdminBiz();
                ResultT<String> registryResult = adminBiz.registryRemove(registryParam);
                if (registryResult != null && ResultT.SUCCESS_CODE == registryResult.getCode()) {
                    log.info("移除节点成功");
                } else {
                    log.info("移除节点失败");
                }
            } catch (Exception e) {
                if (!toStop) {
                    log.error(e.getMessage(), e);
                }
            }
            log.info("executorRegistryThread 销毁成功");
        });
        registryThread.setDaemon(true);
        registryThread.setName("executorRegistryThread");
        registryThread.start();
        log.info("注册节点守护线程启动成功");
    }

    /**
     * 停止 ExecutorRegistryThread 线程
     */
    public void stop() {
        toStop = true;
        registryThread.interrupt();

        try {
            registryThread.join();
        } catch (InterruptedException e) {
            log.error(e.getMessage(), e);
        }
        log.info("注册节点守护线程停止成功");
    }

}
