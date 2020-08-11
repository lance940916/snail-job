package com.snailwu.job.admin.core.thread;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author 吴庆龙
 * @date 2020/7/21 5:05 下午
 */
public class JobLogReportHelper {
    public static final Logger log = LoggerFactory.getLogger(JobLogReportHelper.class);

    private static Thread logReportThread;
    private static volatile boolean toStop = false;

    public void start() {
        logReportThread = new Thread(() -> {
            while (!toStop) {
                for (int i = 0; i < 3; i++) {

                }
            }
        });
        logReportThread.setDaemon(true);
        logReportThread.setName("JobLogReportThread");
        logReportThread.start();
    }

    public void stop() {
        logReportThread.interrupt();
        try {
            logReportThread.join();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

}
