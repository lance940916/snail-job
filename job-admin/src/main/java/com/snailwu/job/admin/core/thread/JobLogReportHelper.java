package com.snailwu.job.admin.core.thread;

import com.snailwu.job.admin.core.config.AdminConfig;
import com.snailwu.job.admin.model.JobLog;
import com.snailwu.job.admin.model.JobLogReport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Calendar;
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
        long startTs = System.currentTimeMillis();

        // 当前天
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.MILLISECOND, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.HOUR_OF_DAY, 0);

        // 触发的开始结束时间
        Date beginTime = cal.getTime();
        cal.add(Calendar.DAY_OF_YEAR, 1);
        Date endTime = cal.getTime();

        // 不断统计当天的任务执行情况
        List<JobLog> jobLogs = AdminConfig.getInstance().getJobLogMapper().selectTodayLogs(beginTime, endTime);
        int successCount = 0;
        int failCount = 0;
        int runningCount = 0;
        for (JobLog log : jobLogs) {
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
        logger.info("今日任务运行状态统计。运行中:{};成功:{};失败:{}", runningCount, successCount, failCount);

        // 更新 job_log_report
        JobLogReport jobLogReport = AdminConfig.getInstance().getJobLogReportMapper().selectTodayReport(beginTime);
        if (jobLogReport == null) {
            jobLogReport = new JobLogReport();
            jobLogReport.setTriggerDay(beginTime);
            jobLogReport.setRunningCount(runningCount);
            jobLogReport.setSuccessCount(successCount);
            jobLogReport.setFailCount(failCount);
            AdminConfig.getInstance().getJobLogReportMapper().insert(jobLogReport);
        } else {
            jobLogReport.setRunningCount(runningCount);
            jobLogReport.setSuccessCount(successCount);
            jobLogReport.setFailCount(failCount);
            AdminConfig.getInstance().getJobLogReportMapper().updateByPrimaryKey(jobLogReport);
        }
        long costMs = System.currentTimeMillis() - startTs;

        // 每分钟执行一次
        if (running) {
            TimeUnit.MILLISECONDS.sleep(1000 * 60);
        }
    }

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
                    logger.error("监控失败任务线程异常。", e);
                }
            }
        });
        thread.setDaemon(true);
        thread.setName("log-report");
        thread.start();
        logger.info("日志报告线程-已启动。");
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
