package com.snailwu.job.admin.core.config;

import com.snailwu.job.admin.core.alarm.JobAlarmComposite;
import com.snailwu.job.admin.core.scheduler.JobScheduler;
import com.snailwu.job.admin.mapper.*;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author 吴庆龙
 * @date 2020/6/8 1:49 下午
 */
@Component
@PropertySource("classpath:application.properties")
public class AdminConfig implements InitializingBean, DisposableBean {
    private static AdminConfig instance;

    public static AdminConfig getInstance() {
        return instance;
    }

    @Value("${access-token}")
    private String accessToken;

    public String getAccessToken() {
        return accessToken;
    }

    // ---------------------- JobScheduler ----------------------

    private JobScheduler jobScheduler;

    @Override
    public void afterPropertiesSet() {
        instance = this;

        // 启动所有守护线程
        jobScheduler = new JobScheduler();
        jobScheduler.startAll();
    }

    @Override
    public void destroy() {
        // 停止所有守护线程
        jobScheduler.stopAll();
    }

    // ---------------------- mapper ----------------------

    @Resource
    private JobNodeMapper jobNodeMapper;
    @Resource
    protected JobAppMapper jobAppMapper;
    @Resource
    private JobInfoMapper jobInfoMapper;
    @Resource
    private JobLockMapper jobLockMapper;
    @Resource
    private JobLogMapper jobLogMapper;
    @Resource
    private JobLogReportMapper jobLogReportMapper;
    @Resource
    private JobAlarmComposite jobAlarmComposite;

    public JobNodeMapper getJobNodeMapper() {
        return jobNodeMapper;
    }

    public JobAppMapper getJobAppMapper() {
        return jobAppMapper;
    }

    public JobInfoMapper getJobInfoMapper() {
        return jobInfoMapper;
    }

    public JobLockMapper getJobLockMapper() {
        return jobLockMapper;
    }

    public JobLogMapper getJobLogMapper() {
        return jobLogMapper;
    }

    public JobLogReportMapper getJobLogReportMapper() {
        return jobLogReportMapper;
    }

    public JobAlarmComposite getJobAlarmComposite() {
        return jobAlarmComposite;
    }

}
