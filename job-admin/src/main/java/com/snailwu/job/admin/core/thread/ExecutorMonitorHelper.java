package com.snailwu.job.admin.core.thread;

import com.snailwu.job.admin.core.config.AdminConfig;
import com.snailwu.job.admin.model.JobApp;
import com.snailwu.job.admin.model.JobExecutor;
import com.snailwu.job.core.constants.CoreConstant;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.TimeUnit;

import static com.snailwu.job.core.constants.CoreConstant.DEAD_TIME;
import static com.snailwu.job.core.enums.RegistryType.AUTO;

/**
 * @author 吴庆龙
 * @date 2020/6/4 11:23 上午
 */
public class ExecutorMonitorHelper {
    private static final Logger logger = LoggerFactory.getLogger(ExecutorMonitorHelper.class);

    private static Thread thread;
    private static volatile boolean running = true;

    private static void run() throws InterruptedException {
        long curTs = System.currentTimeMillis();

        // 删除不活跃的执行器
        Date deadDate = new Date(curTs - DEAD_TIME);
        AdminConfig.getInstance().getJobExecutorMapper().deleteDead(deadDate);

        // 获取所有自动注册的应用
        List<JobApp> apps = AdminConfig.getInstance().getJobAppMapper().selectAutoRegistry(AUTO.getValue());

        // 获取所有的执行器
        List<JobExecutor> executors = AdminConfig.getInstance().getJobExecutorMapper().selectAllWithoutTime();

        // 根据应用合并执行器地址
        Map<String, List<String>> appAddressMap = new HashMap<>();
        for (JobExecutor executor : executors) {
            String appName = executor.getAppName();
            String address = executor.getAddress().toLowerCase();
            List<String> addresses = appAddressMap.computeIfAbsent(appName, k -> new ArrayList<>());
            if (!addresses.contains(address)) {
                addresses.add(address);
            }
        }

        // 更新应用中的执行器地址
        Date updateDate = new Date(curTs);
        for (JobApp app : apps) {
            List<String> addressList = appAddressMap.get(app.getAppName());
            String addresses = StringUtils.join(addressList, ",");

            // 通过id更新地址列表
            app.setAddresses(addresses);
            app.setUpdateTime(updateDate);
            AdminConfig.getInstance().getJobAppMapper().updateAddressesById(app);
            logger.info("应用名称：{} 地址列表：{}", app.getAppName(), addresses);
        }

        // 休眠
        if (running) {
            TimeUnit.SECONDS.sleep(10);
        }
    }

    /**
     * 启动整理注册器线程
     */
    public static void start() {
        // TODO 重复代码待重构
        thread = new Thread(() -> {
            while (running) {
                try {
                    run();
                } catch (InterruptedException e) {
                    if (running) {
                        logger.error("线程：{} 休眠异常。", thread.getName(), e);
                    }
                } catch (Exception e) {
                    logger.error("整理节点异常。", e);
                }
            }
        });
        thread.setDaemon(true);
        thread.setName("executor-monitor");
        thread.start();
    }

    /**
     * 停止注册
     */
    public static void stop() {
        running = false;
        try {
            thread.interrupt();
            thread.join();
        } catch (InterruptedException e) {
            logger.error("停止线程 {} 异常", thread.getName(), e);
        }
    }

}
