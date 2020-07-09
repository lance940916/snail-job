package com.snailwu.job.admin.core.thread;

import com.snailwu.job.admin.core.conf.AdminConfig;
import com.snailwu.job.admin.core.cron.CronExpression;
import com.snailwu.job.admin.core.model.JobInfo;
import org.mybatis.dynamic.sql.render.RenderingStrategies;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import static com.snailwu.job.admin.mapper.JobInfoDynamicSqlSupport.*;
import static org.mybatis.dynamic.sql.SqlBuilder.*;

/**
 * @author 吴庆龙
 * @date 2020/7/9 2:40 下午
 */
public class JobScheduleHelper {
    private static final Logger log = LoggerFactory.getLogger(JobScheduleHelper.class);

    private static final JobScheduleHelper helper = new JobScheduleHelper();

    public static JobScheduleHelper getInstance() {
        return helper;
    }

    private Thread scheduleThread;
    private volatile boolean stopFlag = false;

    private static final long PRE_READ_MS = 5000;
    private static final Map<Integer, List<Integer>> ringData = new ConcurrentHashMap<>();

    public void start() {
        scheduleThread = new Thread(() -> {
            // 对齐整秒
            long sleepTs = 1000 - System.currentTimeMillis() % 1000;
            try {
                TimeUnit.MILLISECONDS.sleep(sleepTs);
            } catch (InterruptedException e) {
                log.error("对齐整秒，休眠异常", e);
            }
            log.info("初始化 Schedule Thread 成功");

            // 一次性读取出来的数量. ThreadPool-Size
            int preReadCount = (200 + 100) * 20;
            while (!stopFlag) {
                long startTs = System.currentTimeMillis();

                // TODO 获取一个分布式锁，用来控制只有一个调度中心进行任务调度

                try {
                    // 1 预读取任务
                    long nowTimeTs = System.currentTimeMillis();
                    List<JobInfo> jobInfoList = AdminConfig.getInstance().getJobInfoMapper().selectMany(
                            select(jobInfo.allColumns())
                                    .from(jobInfo)
                                    .where(triggerStatus, isEqualTo((byte) 1))
                                    .and(triggerNextTime, isLessThanOrEqualTo(nowTimeTs + PRE_READ_MS))
                                    .limit(preReadCount)
                                    .build().render(RenderingStrategies.MYBATIS3)
                    );
                    // 预读取是否成功
                    boolean preReadSuccess = !jobInfoList.isEmpty();

                    for (JobInfo info : jobInfoList) {
                        if (nowTimeTs > info.getTriggerNextTime() + PRE_READ_MS) {
                            // 错过了任务的执行时间：忽略并刷新任务的下次触发时间
                            log.warn("定时器错过执行任务. jobId:{}", info.getId());
                            refreshNextValidTime(info, new Date());

                        } else if (nowTimeTs > info.getTriggerNextTime()) {
                            // 任务执行时间过时了 <= 5秒：直接执行并刷新任务的下次触发时间
                            JobTriggerPoolHelper.getInstance().addTriggerPool(info.getId(), -1, info.getExecutorParam());

                            refreshNextValidTime(info, new Date());

                            // 下次触发时间在 5 秒内
                            if (info.getTriggerStatus() == 1 && nowTimeTs + PRE_READ_MS > info.getTriggerNextTime()) {
                                int ringSecond = (int) ((info.getTriggerNextTime() / 1000) % 60);

                                pushTimeRing(ringSecond, info.getId());

                                refreshNextValidTime(info, new Date(info.getTriggerNextTime()));
                            }
                        } else {
                            int ringSecond = (int) ((info.getTriggerNextTime() / 1000) % 60);

                            pushTimeRing(ringSecond, info.getId());

                            refreshNextValidTime(info, new Date(info.getTriggerNextTime()));
                        }
                    }

                    // 更新任务信息
                    for (JobInfo info : jobInfoList) {
                        AdminConfig.getInstance().getJobInfoMapper().update(
                                update(jobInfo)
                                        .set(triggerLastTime).equalTo(info.getTriggerLastTime())
                                        .set(triggerNextTime).equalTo(info.getTriggerNextTime())
                                        .set(triggerStatus).equalTo(info.getTriggerStatus())
                                        .where(id, isEqualTo(info.getId()))
                                        .build().render(RenderingStrategies.MYBATIS3)
                        );
                    }
                } catch (ParseException e) {
                    if (!stopFlag) {
                        log.error("JobScheduleHelper Thread Error.", e);
                    }
                }


            }
        });
    }

    private void refreshNextValidTime(JobInfo info, Date fromDate) throws ParseException {
        Date nextValidDate = new CronExpression(info.getCron()).getNextValidTimeAfter(fromDate);
        if (nextValidDate != null) {
            info.setTriggerLastTime(info.getTriggerNextTime());
            info.setTriggerNextTime(nextValidDate.getTime());
        } else {
            info.setTriggerStatus((byte) 0);
            info.setTriggerLastTime((long) 0);
            info.setTriggerNextTime((long) 0);
        }
    }

    private void pushTimeRing(int ringSecond, int jobId) {
        List<Integer> ringList = ringData.computeIfAbsent(ringSecond, k -> new ArrayList<>());
        ringList.add(jobId);
    }

}
