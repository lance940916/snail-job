package com.snailwu.job.admin.core.thread;

import com.snailwu.job.admin.core.config.AdminConfig;
import com.snailwu.job.admin.model.JobLog;
import com.snailwu.job.admin.model.JobLogReport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 统计每天的任务执行结果数量
 *
 * @author 吴庆龙
 * @date 2020/7/21 5:05 下午
 */
public class JobLogReportHelper {
    private static final Logger logger = LoggerFactory.getLogger(JobLogReportHelper.class);

    private static Thread thread;
    private static volatile boolean running = true;

    private static void run() throws InterruptedException {
        // 当前日期
        LocalDate date = LocalDate.now();
        Date beginDate = Date.from(date.atStartOfDay().toInstant(ZoneOffset.ofHours(8)));
        Date endDate = Date.from(date.plusDays(1).atStartOfDay().toInstant(ZoneOffset.ofHours(8)));

        // 不断统计当天的任务执行情况
        List<JobLog> logs = AdminConfig.getInstance().getJobLogMapper().selectTodayLogs(beginDate, endDate);
        int successCount = 0;
        int failCount = 0;
        int runningCount = 0;
        for (JobLog log : logs) {
            Integer triggerCode = log.getTriggerCode();
            Integer execCode = log.getExecCode();
            if (triggerCode == 200 && execCode == 200) {
                successCount++;
            } else if (triggerCode == 200 && execCode == 0) {
                runningCount++;
            } else {
                failCount++;
            }
        }
        logger.info("任务运行状态统计：运行中[{}] 成功[{}] 失败[{}]", runningCount, successCount, failCount);

        // 更新 job_log_report
        JobLogReport logReport = AdminConfig.getInstance().getJobLogReportMapper().selectTodayReport(beginDate);
        if (logReport == null) {
            logReport = new JobLogReport();
            logReport.setTriggerDay(beginDate);
            logReport.setSuccessCount(successCount);
            logReport.setRunningCount(runningCount);
            logReport.setFailCount(failCount);
            AdminConfig.getInstance().getJobLogReportMapper().insert(logReport);
        } else {
            logReport.setSuccessCount(successCount);
            logReport.setRunningCount(runningCount);
            logReport.setFailCount(failCount);
            AdminConfig.getInstance().getJobLogReportMapper().updateByPrimaryKey(logReport);
        }

        // 每分钟执行一次
        if (running) {
            TimeUnit.MILLISECONDS.sleep(1000 * 60);
        }
    }

    public static void start() {
        thread = new Thread(() -> {
            while (running) {
                try {
                    run();
                } catch (InterruptedException e) {
                    if (running) {
                        logger.error("线程：{} 休眠异常。", thread.getName(), e);
                    }
                } catch (Exception e) {
                    logger.error("监控失败任务线程异常。", e);
                }
            }
        });
        thread.setDaemon(true);
        thread.setName("log-report");
        thread.start();
    }

    public static void stop() {
        running = false;
        try {
            thread.interrupt();
            thread.join();
        } catch (Exception e) {
            logger.error("停止线程 {} 异常", thread.getName(), e);
        }
    }

}
