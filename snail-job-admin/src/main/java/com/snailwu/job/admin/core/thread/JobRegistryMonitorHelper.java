package com.snailwu.job.admin.core.thread;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author 吴庆龙
 * @date 2020/6/4 11:23 上午
 */
public class JobRegistryMonitorHelper {
    public static final Logger log = LoggerFactory.getLogger(JobRegistryMonitorHelper.class);

    private static final JobRegistryMonitorHelper instance = new JobRegistryMonitorHelper();
    public static JobRegistryMonitorHelper getInstance() {
        return instance;
    }

    private Thread registryThread;
    private volatile boolean toStop = false;

    public void start() {
        registryThread = new Thread(() ->{
            while (!toStop) {

            }
        });
        registryThread.setDaemon(true);
        registryThread.setName("JobRegistryThread");
        registryThread.start();
    }

}
