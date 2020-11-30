package com.snailwu.job.admin.core.thread;

import com.snailwu.job.admin.core.config.AdminConfig;
import com.snailwu.job.admin.mapper.JobLogDynamicSqlSupport;
import com.snailwu.job.admin.mapper.JobLogReportDynamicSqlSupport;
import com.snailwu.job.admin.model.JobLog;
import com.snailwu.job.admin.model.JobLogReport;
import org.mybatis.dynamic.sql.render.RenderingStrategies;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.mybatis.dynamic.sql.SqlBuilder.*;

/**
 * @author 吴庆龙
 * @date 2020/7/21 5:05 下午
 */
public class JobLogReportHelper {
    private static final Logger LOGGER = LoggerFactory.getLogger(JobLogReportHelper.class);

    private static Thread logReportThread;
    private static volatile boolean running = true;

    public static void start() {
        logReportThread = new Thread(() -> {
            while (running) {
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
                List<JobLog> jobLogs = listJobLogs(beginTime, endTime);
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
                LOGGER.info("今日任务运行状态统计。运行中:{};成功:{};失败:{}", runningCount, successCount, failCount);

                // 更新 job_log_report
                JobLogReport jobLogReport = AdminConfig.getInstance().getJobLogReportMapper().selectOne(
                        select(JobLogReportDynamicSqlSupport.jobLogReport.allColumns())
                                .from(JobLogReportDynamicSqlSupport.jobLogReport)
                                .where()
                                .and(JobLogReportDynamicSqlSupport.triggerDay, isEqualTo(beginTime))
                                .build().render(RenderingStrategies.MYBATIS3)
                ).orElse(null);

                JobLogReport report = new JobLogReport();
                report.setRunningCount(runningCount);
                report.setSuccessCount(successCount);
                report.setFailCount(failCount);
                report.setTriggerDay(beginTime);
                if (jobLogReport == null) {
                    // 新增
                    AdminConfig.getInstance().getJobLogReportMapper().insertSelective(report);
                } else {
                    // 更新
                    report.setId(jobLogReport.getId());
                    AdminConfig.getInstance().getJobLogReportMapper().updateByPrimaryKeySelective(report);
                }
                long costMs = System.currentTimeMillis() - startTs;

                // 每分钟执行一次
                try {
                    TimeUnit.MILLISECONDS.sleep(60000 - costMs);
                } catch (InterruptedException e) {
                    if (!running) {
                        LOGGER.error("任务扫描整理线程。休眠异常", e);
                    }
                }
            }
        });
        logReportThread.setDaemon(true);
        logReportThread.setName("JobLogReportThread");
        logReportThread.start();
        LOGGER.info("日志报告线程-已启动。");
    }

    /**
     * 列出任务的日志
     * @param beginTime 开始时间
     * @param endTime 结束时间
     * @return 日志列表
     */
    private static List<JobLog> listJobLogs(Date beginTime, Date endTime) {
        return AdminConfig.getInstance().getJobLogMapper().selectMany(
                select(JobLogDynamicSqlSupport.id, JobLogDynamicSqlSupport.triggerCode, JobLogDynamicSqlSupport.execCode)
                        .from(JobLogDynamicSqlSupport.jobLog)
                        .where()
                        .and(JobLogDynamicSqlSupport.triggerTime, isGreaterThanOrEqualTo(beginTime))
                        .and(JobLogDynamicSqlSupport.triggerTime, isLessThan(endTime))
                        .build().render(RenderingStrategies.MYBATIS3)
        );
    }

    public static void stop() {
        running = false;
        logReportThread.interrupt();
        try {
            logReportThread.join();
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

}
