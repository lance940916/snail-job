package com.snailwu.job.admin.core.scheduler;

import com.snailwu.job.admin.core.conf.AdminConfig;
import com.snailwu.job.admin.core.thread.ExecutorRegistryMonitorHelper;
import com.snailwu.job.admin.core.thread.JobScheduleHelper;
import com.snailwu.job.core.biz.ExecutorBiz;
import com.snailwu.job.core.biz.client.ExecutorBizClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author 吴庆龙
 * @date 2020/6/4 11:19 上午
 */
public class SnailJobScheduler {
    private static final Logger log = LoggerFactory.getLogger(SnailJobScheduler.class);

    /**
     * 初始化
     */
    public void init() {
        // 定时整理注册节点到 group 中
        ExecutorRegistryMonitorHelper.start();

        // 启动任务扫描类
        JobScheduleHelper.start();
    }

    /**
     * 销毁
     */
    public void destroy() {
        ExecutorRegistryMonitorHelper.stop();

        JobScheduleHelper.stop();
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
