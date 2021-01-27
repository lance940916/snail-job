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
public class JobScheduler {

    /**
     * 启动线程
     * 注意顺序
     */
    public void startAll() {
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
    public void stopAll() {
        JobScheduleHelper.stop();

        JobTriggerPoolHelper.stop();

        JobLogReportHelper.stop();

        JobFailMonitorHelper.stop();

        ExecutorMonitorHelper.stop();
    }

    /**
     * key: 节点的地址
     * value: 节点调用类
     */
    private static final ConcurrentHashMap<String, ExecutorBiz> EXECUTOR_BIZ_REPOSITORY = new ConcurrentHashMap<>();

    /**
     * 根据地址获取 ExecutorBiz
     */
    public static ExecutorBiz obtainOrCreateExecutorBiz(String address) {
        if (address == null || address.trim().length() == 0) {
            return null;
        }
        address = address.trim();
        ExecutorBiz executorBiz = EXECUTOR_BIZ_REPOSITORY.get(address);
        if (executorBiz != null) {
            return executorBiz;
        }
        executorBiz = new ExecutorBizClient(address, AdminConfig.getInstance().getAccessToken());
        EXECUTOR_BIZ_REPOSITORY.put(address, executorBiz);
        return executorBiz;
    }

}
