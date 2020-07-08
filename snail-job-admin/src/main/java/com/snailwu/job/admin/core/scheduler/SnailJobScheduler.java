package com.snailwu.job.admin.core.scheduler;

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
//        ExecutorRegistryMonitorHelper.getInstance().start();
    }

    /**
     * 销毁
     */
    public void destroy() {

    }

    // Executor-Client
    private static final ConcurrentHashMap<String, ExecutorBiz> EXECUTOR_BIZ_REPOSITORY = new ConcurrentHashMap<>();

    public static ExecutorBiz getExecutorBiz(String address) {
        if (address == null || address.trim().length() == 0) {
            return null;
        }

        address = address.trim();
        ExecutorBiz executorBiz = EXECUTOR_BIZ_REPOSITORY.get(address);
        if (executorBiz != null) {
            return executorBiz;
        }

        executorBiz = new ExecutorBizClient(address);
        EXECUTOR_BIZ_REPOSITORY.put(address, executorBiz);
        return null;
    }

}
