package com.snailwu.job.admin.core.thread;

import com.snailwu.job.admin.core.conf.AdminConfig;
import com.snailwu.job.admin.core.model.JobInfo;
import com.snailwu.job.admin.core.model.JobLog;
import com.snailwu.job.admin.mapper.JobInfoDynamicSqlSupport;
import com.snailwu.job.admin.mapper.JobLogDynamicSqlSupport;
import com.snailwu.job.admin.trigger.TriggerTypeEnum;
import org.mybatis.dynamic.sql.render.RenderingStrategies;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

import static com.snailwu.job.core.enums.AlarmStatus.*;
import static org.mybatis.dynamic.sql.SqlBuilder.*;

/**
 * 监控失败的调度
 * 1. 失败重试
 * 2. 告警
 *
 * @author 吴庆龙
 * @date 2020/7/20 10:40 上午
 */
public class JobFailMonitorHelper {
    private static final Logger log = LoggerFactory.getLogger(JobFailMonitorHelper.class);

    private static Thread monitorThread;
    private static volatile boolean toStop = false;

    public static void start() {
        monitorThread = new Thread(() -> {
            while (!toStop) {
                // (调度不成功 | 执行不成功的任务) & 为告警的任务日志
                List<JobLog> jobLogList = AdminConfig.getInstance().getJobLogMapper().selectMany(
                        select(JobLogDynamicSqlSupport.jobLog.allColumns()).from(JobLogDynamicSqlSupport.jobLog)
                                .where()
                                // 调度不成功
                                .and(JobLogDynamicSqlSupport.triggerCode, isNotEqualTo(200))
                                // 执行不成功
                                .or(JobLogDynamicSqlSupport.execCode, isNotIn(0, 200))
                                .and(JobLogDynamicSqlSupport.alarmStatus, isEqualTo(DEFAULT.getValue()))
                                .build().render(RenderingStrategies.MYBATIS3)
                );
                for (JobLog jobLog : jobLogList) {
                    // lock
                    int update = AdminConfig.getInstance().getJobLogMapper().update(
                            update(JobLogDynamicSqlSupport.jobLog)
                                    .set(JobLogDynamicSqlSupport.alarmStatus).equalTo((byte) -1)
                                    .where()
                                    .and(JobLogDynamicSqlSupport.id, isEqualTo(jobLog.getId()))
                                    .and(JobLogDynamicSqlSupport.alarmStatus, isEqualTo(DEFAULT.getValue()))
                                    .build().render(RenderingStrategies.MYBATIS3)
                    );
                    if (update < 1) {
                        continue;
                    }
                    Optional<JobInfo> optionalJobInfo = AdminConfig.getInstance().getJobInfoMapper().selectOne(
                            select(JobInfoDynamicSqlSupport.jobInfo.allColumns())
                                    .from(JobInfoDynamicSqlSupport.jobInfo)
                                    .where(JobInfoDynamicSqlSupport.id, isEqualTo(jobLog.getJobId()))
                                    .build().render(RenderingStrategies.MYBATIS3)
                    );

                    // 失败重试,每次重试都要将这个字段 -1
                    if (jobLog.getExecutorFailRetryCount() > 0) {
                        int finalFailRetryCount = jobLog.getExecutorFailRetryCount() - 1;
                        JobTriggerPoolHelper.push(jobLog.getJobId(), TriggerTypeEnum.RETRY, finalFailRetryCount,
                                jobLog.getExecutorParam());
                    }

                    // 失败告警
                    int newAlarmStatus = NOT_ALARM.getValue(); // 告警状态：0-默认、-1=锁定状态、1-无需告警、2-告警成功、3-告警失败
                    if (optionalJobInfo.isPresent()) {
                        JobInfo jobInfo = optionalJobInfo.get();
                        if (jobInfo.getAlarmEmail() != null && jobInfo.getAlarmEmail().trim().length() > 0) {
                            boolean alarmResult = AdminConfig.getInstance().getJobAlarmComposite().alarm(jobInfo, jobLog);
                            newAlarmStatus = alarmResult ? ALARM_SUCCESS.getValue() : ALARM_FAIL.getValue();
                        }
                    }

                    // unlock
                    AdminConfig.getInstance().getJobLogMapper().update(
                            update(JobLogDynamicSqlSupport.jobLog)
                                    .set(JobLogDynamicSqlSupport.alarmStatus).equalTo((byte) newAlarmStatus)
                                    .where(JobLogDynamicSqlSupport.id, isEqualTo(jobLog.getId()))
                                    .and(JobLogDynamicSqlSupport.alarmStatus, isEqualTo(LOCK.getValue()))
                                    .build().render(RenderingStrategies.MYBATIS3)
                    );
                }


            }
        });
        monitorThread.setDaemon(true);
        monitorThread.setName("JobFailMonitor");
        monitorThread.start();
    }

    public static void stop() {
        toStop = true;

        monitorThread.interrupt();
        try {
            monitorThread.join();
        } catch (InterruptedException e) {
            log.error(e.getMessage(), e);
        }
    }
}
