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
 * 向调度中心注册 Worker 节点
 *
 * @author 吴庆龙
 * @date 2020/5/25 4:00 下午
 */
public class ExecutorRegistryThread {
    public static final Logger log = LoggerFactory.getLogger(ExecutorRegistryThread.class);

    private static final ExecutorRegistryThread instance = new ExecutorRegistryThread();

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
            log.warn("注册失败. 没有配置 appName");
            return;
        }
        if (SnailJobExecutor.getAdminBiz() == null) {
            log.warn("注册失败. 未配置调度中心地址");
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
                        log.info("在调度中心注册成功");
                    } else {
                        log.error("在调度中心注册失败");
                    }
                } catch (Exception e) {
                    if (!toStop) {
                        log.error("在调度中心注册异常", e);
                    }
                }

                // 线程没有停止，则进行休眠
                try {
                    if (!toStop) {
                        TimeUnit.SECONDS.sleep(RegistryConfig.BEAT_TIMEOUT);
                    }
                } catch (InterruptedException e) {
                    if (!toStop) {
                        log.error("休眠被中断，且 toStop 为 false. errMsg:{}", e.getMessage());
                    }
                }
            }

            // 移除节点。线程被停止后（toStop 为 true）通知调度中心进行节点的移除
            try {
                RegistryParam registryParam = new RegistryParam(EXECUTOR.name(), appName, address);
                AdminBiz adminBiz = SnailJobExecutor.getAdminBiz();
                ResultT<String> registryResult = adminBiz.registryRemove(registryParam);
                if (registryResult != null && ResultT.SUCCESS_CODE == registryResult.getCode()) {
                    log.info("通知调度中心移除注册节点成功");
                } else {
                    log.info("通知调度中心移除注册节点失败");
                }
            } catch (Exception e) {
                if (!toStop) {
                    log.error("通知调度中心移除注册节点异常", e);
                }
            }
        });
        registryThread.setDaemon(true);
        registryThread.setName("executorRegistryThread");
        registryThread.start();
        log.info("注册节点守护线程 启动成功");
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
        log.info("注册节点守护线程 停止成功");
    }

}
