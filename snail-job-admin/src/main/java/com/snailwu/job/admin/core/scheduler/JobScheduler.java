package com.snailwu.job.admin.core.scheduler;

import com.snailwu.job.admin.core.config.AdminConfig;
import com.snailwu.job.admin.core.thread.*;
import com.snailwu.job.core.biz.NodeBiz;
import com.snailwu.job.core.biz.client.NodeBizClient;

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
        NodeMonitorHelper.start();

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

        NodeMonitorHelper.stop();

        JobLogReportHelper.stop();

        JobFailMonitorHelper.stop();
    }

    /**
     * key: 节点的地址
     * value: 节点调用类
     */
    private static final ConcurrentHashMap<String, NodeBiz> NODE_BIZ_REPOSITORY = new ConcurrentHashMap<>();

    /**
     * 根据地址 获取 NodeBiz
     */
    public static NodeBiz obtainOrCreateNodeBiz(String address) {
        if (address == null || address.trim().length() == 0) {
            return null;
        }
        address = address.trim();
        NodeBiz nodeBiz = NODE_BIZ_REPOSITORY.get(address);
        if (nodeBiz != null) {
            return nodeBiz;
        }
        nodeBiz = new NodeBizClient(address, AdminConfig.getInstance().getAccessToken());
        NODE_BIZ_REPOSITORY.put(address, nodeBiz);
        return nodeBiz;
    }

}
