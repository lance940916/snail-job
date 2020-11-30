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

import static com.snailwu.job.core.biz.model.ResultT.SUCCESS_CODE;
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

    private static Thread thread;
    private static volatile boolean running = true;

    /**
     * 启动线程
     */
    public static void start() {
        thread = new Thread(() -> {
            while (running) {
                try {
                    // 未告警的任务日志 && 已经有执行结果 || 调度失败的
                    List<JobLog> jobLogs = listNeedAlarmLog();
                    for (JobLog log : jobLogs) {
                        // 锁定这条数据，依赖数据库的锁
                        int update = lock(log);
                        if (update < 1) {
                            // 锁定失败不操作
                            continue;
                        }

                        // 查询任务的信息
                        JobInfo jobInfo = AdminConfig.getInstance().getJobInfoMapper().selectOne(
                                select(JobInfoDynamicSqlSupport.jobInfo.allColumns())
                                        .from(JobInfoDynamicSqlSupport.jobInfo)
                                        .where(JobInfoDynamicSqlSupport.id, isEqualTo(log.getJobId()))
                                        .build().render(RenderingStrategies.MYBATIS3)
                        ).orElse(null);
                        if (jobInfo == null) {
                            LOGGER.error("无此任务。任务:{}", log.getJobId());
                            // 解锁，更新告警状态为无需告警
                            unlock(log, NOT_ALARM.getValue());
                            continue;
                        }

                        // 失败重试,每次重试都要将这个字段 -1
                        if (log.getFailRetryCount() > 0) {
                            // 任务指定的重试次数
                            int finalFailRetryCount = log.getFailRetryCount() - 1;

                            // 进行任务调度，会增加一条调度日志
                            JobTriggerPoolHelper.push(log.getJobId(), TriggerTypeEnum.RETRY, finalFailRetryCount,
                                    log.getExecParam());
                        }

                        // 进行告警，并获取告警结果，默认无需告警
                        byte newAlarmStatus = NOT_ALARM.getValue();
                        if (jobInfo.getAlarmEmail() != null && jobInfo.getAlarmEmail().trim().length() > 0) {
                            boolean alarmResult = AdminConfig.getInstance().getJobAlarmComposite().alarm(jobInfo, log);
                            newAlarmStatus = alarmResult ? ALARM_SUCCESS.getValue() : ALARM_FAIL.getValue();
                        }

                        // unlock
                        unlock(log, newAlarmStatus);
                    }
                } catch (Exception e) {
                    LOGGER.error("任务失败监控异常", e);
                }

                // 休眠
                if (running) {
                    try {
                        TimeUnit.SECONDS.sleep(1);
                    } catch (InterruptedException e) {
                        LOGGER.error("休眠异常。", e);
                    }
                }
            }
        });
        thread.setDaemon(true);
        thread.setName("fail-monitor-thread");
        thread.start();
        LOGGER.info("失败监控线程-已启动。");
    }

    /**
     * 解锁
     */
    private static void unlock(JobLog log, byte newAlarmStatus) {
        AdminConfig.getInstance().getJobLogMapper().update(
                update(JobLogDynamicSqlSupport.jobLog)
                        .set(JobLogDynamicSqlSupport.alarmStatus).equalTo(newAlarmStatus)
                        .where(JobLogDynamicSqlSupport.id, isEqualTo(log.getId()))
                        .and(JobLogDynamicSqlSupport.alarmStatus, isEqualTo(LOCK.getValue()))
                        .build().render(RenderingStrategies.MYBATIS3)
        );
    }

    /**
     * 加锁
     */
    private static int lock(JobLog log) {
        return AdminConfig.getInstance().getJobLogMapper().update(
                update(JobLogDynamicSqlSupport.jobLog)
                        .set(JobLogDynamicSqlSupport.alarmStatus).equalTo(LOCK.getValue())
                        .where()
                        .and(JobLogDynamicSqlSupport.id, isEqualTo(log.getId()))
                        .and(JobLogDynamicSqlSupport.alarmStatus, isEqualTo(DEFAULT.getValue()))
                        .build().render(RenderingStrategies.MYBATIS3)
        );
    }

    /**
     * 查询需要告警的日志
     */
    private static List<JobLog> listNeedAlarmLog() {
        return AdminConfig.getInstance().getJobLogMapper().selectMany(
                select(JobLogDynamicSqlSupport.id, JobLogDynamicSqlSupport.execCode)
                        .from(JobLogDynamicSqlSupport.jobLog)
                        .where()
                        .and(JobLogDynamicSqlSupport.alarmStatus, isEqualTo(DEFAULT.getValue()))
                        .and(JobLogDynamicSqlSupport.triggerCode, isNotEqualTo(SUCCESS_CODE),
                                or(JobLogDynamicSqlSupport.execCode, isNotEqualTo(SUCCESS_CODE)))
                        .build().render(RenderingStrategies.MYBATIS3)
        );
    }

    /**
     * 停止线程
     */
    public static void stop() {
        running = false;
        thread.interrupt();
        try {
            thread.join();
        } catch (InterruptedException e) {
            LOGGER.error(e.getMessage(), e);
        }
        LOGGER.info("失败监控线程-已停止。");
    }
}
