package com.snailwu.job.admin.core.thread;

import com.snailwu.job.admin.core.config.AdminConfig;
import com.snailwu.job.admin.core.trigger.TriggerTypeEnum;
import com.snailwu.job.admin.mapper.JobInfoDynamicSqlSupport;
import com.snailwu.job.admin.mapper.JobLogDynamicSqlSupport;
import com.snailwu.job.admin.model.JobInfo;
import com.snailwu.job.admin.model.JobLog;
import org.mybatis.dynamic.sql.render.RenderingStrategies;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.snailwu.job.core.enums.AlarmStatus.*;
import static org.mybatis.dynamic.sql.SqlBuilder.*;

/**
 * 监控失败的调度
 * 1. 失败重试(只会重试调度失败的，执行失败的不会重试)
 * 2. 告警
 *
 * @author 吴庆龙
 * @date 2020/7/20 10:40 上午
 */
public class JobFailMonitorHelper {
    private static final Logger LOGGER = LoggerFactory.getLogger(JobFailMonitorHelper.class);

    private JobFailMonitorHelper() {
    }

    private static Thread monitorThread;
    private static volatile boolean stopFlag = false;

    /**
     * 启动线程
     */
    public static void start() {
        monitorThread = new Thread(() -> {
            while (!stopFlag) {
                try {
                    // 调度不成功 & 未告警的任务日志
                    List<JobLog> jobLogList = AdminConfig.getInstance().getJobLogMapper().selectMany(
                            select(JobLogDynamicSqlSupport.id)
                                    .from(JobLogDynamicSqlSupport.jobLog)
                                    .where()
                                    .and(JobLogDynamicSqlSupport.alarmStatus, isEqualTo(DEFAULT.getValue()))
                                    .and(JobLogDynamicSqlSupport.triggerCode, isNotEqualTo(200))
                                    .build().render(RenderingStrategies.MYBATIS3)
                    );
                    for (JobLog jobLog : jobLogList) {
                        // 锁定这条数据，依赖数据库的锁
                        int update = AdminConfig.getInstance().getJobLogMapper().update(
                                update(JobLogDynamicSqlSupport.jobLog)
                                        .set(JobLogDynamicSqlSupport.alarmStatus).equalTo(LOCK.getValue())
                                        .where()
                                        .and(JobLogDynamicSqlSupport.id, isEqualTo(jobLog.getId()))
                                        .and(JobLogDynamicSqlSupport.alarmStatus, isEqualTo(DEFAULT.getValue()))
                                        .build().render(RenderingStrategies.MYBATIS3)
                        );
                        if (update < 1) {
                            // 锁定失败不操作
                            continue;
                        }

                        // 查询任务的信息
                        JobInfo jobInfo = AdminConfig.getInstance().getJobInfoMapper().selectOne(
                                select(JobInfoDynamicSqlSupport.jobInfo.allColumns())
                                        .from(JobInfoDynamicSqlSupport.jobInfo)
                                        .where(JobInfoDynamicSqlSupport.id, isEqualTo(jobLog.getJobId()))
                                        .build().render(RenderingStrategies.MYBATIS3)
                        ).orElse(null);
                        if (jobInfo == null) {
                            LOGGER.error("没有此任务.任务:{}", jobLog.getJobId());
                            // 解锁，更新告警状态为无需告警
                            AdminConfig.getInstance().getJobLogMapper().update(
                                    update(JobLogDynamicSqlSupport.jobLog)
                                            .set(JobLogDynamicSqlSupport.alarmStatus).equalTo(NOT_ALARM.getValue())
                                            .where(JobLogDynamicSqlSupport.id, isEqualTo(jobLog.getId()))
                                            .and(JobLogDynamicSqlSupport.alarmStatus, isEqualTo(LOCK.getValue()))
                                            .build().render(RenderingStrategies.MYBATIS3)
                            );
                            continue;
                        }

                        // 失败重试,每次重试都要将这个字段 -1
                        if (jobLog.getFailRetryCount() > 0) {
                            // 任务指定的重试次数
                            int finalFailRetryCount = jobLog.getFailRetryCount() - 1;
                            // 进行任务调度，并增加一条调度日志
                            JobTriggerPoolHelper.push(jobLog.getJobId(), TriggerTypeEnum.RETRY, finalFailRetryCount,
                                    jobLog.getExecParam());
                        }

                        // 失败告警
                        byte newAlarmStatus = NOT_ALARM.getValue(); // 告警状态：0-默认、-1=锁定状态、1-无需告警、2-告警成功、3-告警失败
                        if (jobInfo.getAlarmEmail() != null && jobInfo.getAlarmEmail().trim().length() > 0) {
                            boolean alarmResult = AdminConfig.getInstance().getJobAlarmComposite().alarm(jobInfo, jobLog);
                            newAlarmStatus = alarmResult ? ALARM_SUCCESS.getValue() : ALARM_FAIL.getValue();
                        }

                        // unlock
                        AdminConfig.getInstance().getJobLogMapper().update(
                                update(JobLogDynamicSqlSupport.jobLog)
                                        .set(JobLogDynamicSqlSupport.alarmStatus).equalTo(newAlarmStatus)
                                        .where(JobLogDynamicSqlSupport.id, isEqualTo(jobLog.getId()))
                                        .and(JobLogDynamicSqlSupport.alarmStatus, isEqualTo(LOCK.getValue()))
                                        .build().render(RenderingStrategies.MYBATIS3)
                        );
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                // 休眠
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    if (!stopFlag) {
                        LOGGER.error("休眠异常.", e);
                    }
                }
            }
        });
        monitorThread.setDaemon(true);
        monitorThread.setName("fail-monitor-thread");
        monitorThread.start();
    }

    /**
     * 停止线程
     */
    public static void stop() {
        stopFlag = true;
        monitorThread.interrupt();
        try {
            monitorThread.join();
        } catch (InterruptedException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }
}
