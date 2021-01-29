package com.snailwu.job.admin.core.thread;

import com.snailwu.job.admin.core.config.AdminConfig;
import com.snailwu.job.admin.core.trigger.TriggerTypeEnum;
import com.snailwu.job.admin.model.JobInfo;
import com.snailwu.job.admin.model.JobLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.snailwu.job.core.biz.model.ResultT.SUCCESS_CODE;
import static com.snailwu.job.core.enums.AlarmStatus.*;

/**
 * 监控失败的调度
 * 1. 失败重试(只会重试调度失败的，执行失败的不会重试)
 * 2. 告警
 *
 * @author 吴庆龙
 * @date 2020/7/20 10:40 上午
 */
public class JobFailMonitorHelper {
    private static final Logger logger = LoggerFactory.getLogger(JobFailMonitorHelper.class);

    private static Thread thread;
    private static volatile boolean running = true;

    private static void run() throws InterruptedException {
        // 查询需要告警的日志
        List<JobLog> logs = AdminConfig.getInstance().getJobLogMapper()
                .selectNeedAlarmLog(DEFAULT.getValue(), SUCCESS_CODE);
        for (JobLog log : logs) {
            // 查询任务的信息
            JobInfo jobInfo = AdminConfig.getInstance().getJobInfoMapper().selectByPrimaryKey(log.getJobId());
            if (jobInfo == null) {
                logger.error("无此任务。任务:{}", log.getJobId());
                // 更新告警状态为无需告警
                AdminConfig.getInstance().getJobLogMapper().updateAlarmStatusById(log.getId(), NOT_ALARM.getValue());
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

            // 更新告警状态
            AdminConfig.getInstance().getJobLogMapper().updateAlarmStatusById(log.getId(), newAlarmStatus);
        }

        // 休眠
        if (running) {
            TimeUnit.SECONDS.sleep(1);
        }
    }

    /**
     * 启动线程
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
                    logger.error("监控失败任务线程异常。", e);
                }
            }
        });
        thread.setDaemon(true);
        thread.setName("fail-monitor");
        thread.start();
    }

    /**
     * 停止线程
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
