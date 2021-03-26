package com.snailwu.job.admin.core.scheduler;

import com.snailwu.job.admin.core.config.AdminConfig;
import com.snailwu.job.admin.core.thread.*;
import com.snailwu.job.core.biz.ExecutorBiz;
import com.snailwu.job.core.biz.client.ExecutorBizClient;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 调度线程的启动与停止
 *
 * @author 吴庆龙
 * @date 2020/6/4 11:19 上午
 */
public abstract class JobScheduler {

    /**
     * 启动线程
     * 注意顺序
     */
    public static void startAll() {
        // 定时整理注册节点到 app 中
        ExecutorMonitorHelper.start();

        // 失败重试线程
        JobFailMonitorHelper.start();

        // 任务运行结果统计线程
        JobLogReportHelper.start();

        // 任务触发类线程
        JobTriggerPoolHelper.start();

        // 启动任务扫描调度类
        JobScheduleHelper.start();
    }

    /**
     * 停止线程
     * 注意顺序
     */
    public static void stopAll() {
        ExecutorMonitorHelper.stop();

        JobScheduleHelper.stop();

        JobTriggerPoolHelper.stop();

        JobLogReportHelper.stop();

        JobFailMonitorHelper.stop();
    }

    // ---------------------------------- 注册调用执行器的类

    /**
     * key: 节点的地址
     * value: 节点调用类
     */
    private static final ConcurrentHashMap<String, ExecutorBiz> EXECUTOR_BIZ_REPOSITORY = new ConcurrentHashMap<>();

    /**
     * 根据地址获取 ExecutorBiz
     */
    public static ExecutorBiz getOrCreateExecutorBiz(String address) {
        ExecutorBiz executorBiz = EXECUTOR_BIZ_REPOSITORY.get(address);
        if (executorBiz != null) {
            return executorBiz;
        }
        executorBiz = new ExecutorBizClient(address, AdminConfig.getInstance().getAccessToken());
        EXECUTOR_BIZ_REPOSITORY.put(address, executorBiz);
        return executorBiz;
    }

}
