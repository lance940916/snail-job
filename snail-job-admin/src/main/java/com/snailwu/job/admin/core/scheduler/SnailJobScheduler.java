package com.snailwu.job.admin.core.scheduler;

import com.snailwu.job.admin.core.conf.AdminConfig;
import com.snailwu.job.admin.core.thread.ExecutorMonitorHelper;
import com.snailwu.job.admin.core.thread.JobScheduleHelper;
import com.snailwu.job.admin.core.thread.JobTriggerPoolHelper;
import com.snailwu.job.core.biz.ExecutorBiz;
import com.snailwu.job.core.biz.client.ExecutorBizClient;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 调度线程的启动与停止
 *
 * @author 吴庆龙
 * @date 2020/6/4 11:19 上午
 */
public class SnailJobScheduler {

    /**
     * 启动线程
     */
    public void init() {
        // 定时整理注册节点到 group 中
        ExecutorMonitorHelper.start();

        // 启动任务扫描调度类
        JobTriggerPoolHelper.start();
        JobScheduleHelper.start();
    }

    /**
     * 停止线程
     */
    public void destroy() {
        ExecutorMonitorHelper.stop();

        JobScheduleHelper.stop();
        JobTriggerPoolHelper.stop();
    }

    // Executor-Client 库
    private static final ConcurrentHashMap<String, ExecutorBiz> EXECUTOR_BIZ_REPOSITORY = new ConcurrentHashMap<>();

    /**
     * 获取访问执行器的biz
     */
    public static ExecutorBiz getExecutorBiz(String address) {
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
