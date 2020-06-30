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
public class ExecutorNodeRegistryThread {
    public static final Logger log = LoggerFactory.getLogger(ExecutorNodeRegistryThread.class);

    /**
     * 单例实例
     */
    private static final ExecutorNodeRegistryThread instance = new ExecutorNodeRegistryThread();

    /**
     * 私有化构造方法
     */
    private ExecutorNodeRegistryThread() {
    }

    /**
     * 获取实例
     */
    public static ExecutorNodeRegistryThread getInstance() {
        return instance;
    }

    /**
     * 注册器线程
     */
    private Thread nodeRegistryThread;

    /**
     * 注册线程停止标志
     */
    private volatile boolean stopFlag = false;

    /**
     * 启动注册线程
     *
     * @param executorName 执行器的唯一标识
     * @param nodeAddress 本机的外网地址 http://ip:port
     */
    public void start(final String executorName, final String nodeAddress) {
        if (StringUtils.isEmpty(executorName)) {
            log.warn("注册失败. 没有配置 executorName");
            return;
        }
        if (SnailJobExecutor.getAdminBiz() == null) {
            log.warn("注册失败. 未配置调度中心地址");
            return;
        }

        // 不断的间隔一定时间进行注册
        nodeRegistryThread = new Thread(() -> {
            while (!stopFlag) {
                try {
                    RegistryParam registryParam = new RegistryParam(executorName, nodeAddress);
                    ResultT<String> registryResult = SnailJobExecutor.getAdminBiz().registry(registryParam);
                    if (registryResult != null && ResultT.SUCCESS_CODE == registryResult.getCode()) {
                        log.info("在调度中心注册成功");
                    } else {
                        log.error("在调度中心注册失败");
                    }
                } catch (Exception e) {
                    if (!stopFlag) {
                        log.error("在调度中心注册异常", e);
                    }
                }

                // 线程没有标记为停止，则进行休眠
                try {
                    if (!stopFlag) {
                        TimeUnit.SECONDS.sleep(RegistryConfig.BEAT_TIMEOUT);
                    }
                } catch (InterruptedException e) {
                    if (!stopFlag) {
                        log.error("休眠被中断，且 toStop 为 false. errMsg:{}", e.getMessage());
                    }
                }
            }

            // 移除节点。线程被停止后（toStop 为 true）通知调度中心进行节点的移除
            try {
                RegistryParam registryParam = new RegistryParam(executorName, nodeAddress);
                ResultT<String> registryResult = SnailJobExecutor.getAdminBiz().registryRemove(registryParam);
                if (registryResult != null && ResultT.SUCCESS_CODE == registryResult.getCode()) {
                    log.info("通知调度中心移除注册节点成功");
                } else {
                    log.info("通知调度中心移除注册节点失败");
                }
            } catch (Exception e) {
                if (!stopFlag) {
                    log.error("通知调度中心移除注册节点异常", e);
                }
            }
        });
        nodeRegistryThread.setDaemon(true);
        nodeRegistryThread.setName("executorRegistryThread");
        nodeRegistryThread.start();
        log.info("注册节点守护线程 启动成功");
    }

    public void stop() {
        stopFlag = true;
        nodeRegistryThread.interrupt();

        try {
            nodeRegistryThread.join();
        } catch (InterruptedException e) {
            log.error(e.getMessage(), e);
        }
        log.info("注册节点守护线程 停止成功");
    }

}
