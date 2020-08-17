package com.snailwu.job.admin.core.thread;

import com.snailwu.job.admin.core.conf.AdminConfig;
import com.snailwu.job.admin.core.model.JobLog;
import com.snailwu.job.admin.core.model.JobLogReport;
import com.snailwu.job.admin.mapper.JobLogDynamicSqlSupport;
import com.snailwu.job.admin.mapper.JobLogReportDynamicSqlSupport;
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
    private static volatile boolean stopFlag = false;

    public static void start() {
        logReportThread = new Thread(() -> {
            while (!stopFlag) {
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
                List<JobLog> jobLogList = AdminConfig.getInstance().getJobLogMapper().selectMany(
                        select(JobLogDynamicSqlSupport.id, JobLogDynamicSqlSupport.triggerCode, JobLogDynamicSqlSupport.execCode)
                                .from(JobLogDynamicSqlSupport.jobLog)
                                .where()
                                .and(JobLogDynamicSqlSupport.triggerTime, isGreaterThanOrEqualTo(beginTime))
                                .and(JobLogDynamicSqlSupport.triggerTime, isLessThan(endTime))
                                .build().render(RenderingStrategies.MYBATIS3)
                );
                int successCount = 0;
                int failCount = 0;
                int runningCount = 0;
                for (JobLog item : jobLogList) {
                    Integer triggerCode = item.getTriggerCode();
                    Integer execCode = item.getExecCode();
                    if (triggerCode == 200 && execCode == 200) {
                        successCount++;
                    } else if (triggerCode == 200 && execCode == 0) {
                        runningCount++;
                    } else {
                        failCount++;
                    }
                }
                LOGGER.info("今日任务运行状态统计.运行中:{};成功:{};失败:{}", runningCount, successCount, failCount);

                // 更新 job_log_report
                JobLogReport jobLogReport = AdminConfig.getInstance().getJobLogReportMapper().selectOne(
                        select(JobLogReportDynamicSqlSupport.jobLogReport.allColumns())
                                .from(JobLogReportDynamicSqlSupport.jobLogReport)
                                .where()
                                .and(JobLogReportDynamicSqlSupport.triggerDay, isEqualTo(beginTime))
                                .build().render(RenderingStrategies.MYBATIS3)
                ).orElse(null);
                if (jobLogReport == null) {
                    // 新增
                    JobLogReport report = new JobLogReport();
                    report.setRunningCount(runningCount);
                    report.setSuccessCount(successCount);
                    report.setFailCount(failCount);
                    report.setTriggerDay(beginTime);
                    AdminConfig.getInstance().getJobLogReportMapper().insertSelective(report);
                } else {
                    runningCount += jobLogReport.getRunningCount();
                    successCount += jobLogReport.getSuccessCount();
                    failCount += jobLogReport.getFailCount();

                    // 更新
                    AdminConfig.getInstance().getJobLogReportMapper().update(
                            update(JobLogReportDynamicSqlSupport.jobLogReport)
                            .set(JobLogReportDynamicSqlSupport.runningCount).equalTo(runningCount)
                            .set(JobLogReportDynamicSqlSupport.successCount).equalTo(successCount)
                            .set(JobLogReportDynamicSqlSupport.failCount).equalTo(failCount)
                            .where(JobLogReportDynamicSqlSupport.triggerDay, isEqualTo(beginTime))
                            .build().render(RenderingStrategies.MYBATIS3)
                    );
                }
                long costMs = System.currentTimeMillis() - startTs;

                // 休眠一分钟
                try {
                    TimeUnit.MILLISECONDS.sleep(60000 - costMs);
                } catch (InterruptedException e) {
                    if (!stopFlag) {
                        LOGGER.error("任务扫描整理线程.休眠异常", e);
                    }
                }
            }
        });
        logReportThread.setDaemon(true);
        logReportThread.setName("JobLogReportThread");
        logReportThread.start();
    }

    public static void stop() {
        stopFlag = true;
        logReportThread.interrupt();
        try {
            logReportThread.join();
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

}
