package com.snailwu.job.admin.core.conf;

import com.snailwu.job.admin.core.alarm.JobAlarmComposite;
import com.snailwu.job.admin.core.scheduler.SnailJobScheduler;
import com.snailwu.job.admin.mapper.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.sql.DataSource;

/**
 * @author 吴庆龙
 * @date 2020/6/8 1:49 下午
 */
@Component
public class AdminConfig implements InitializingBean, DisposableBean {
    private static final Logger LOGGER = LoggerFactory.getLogger(AdminConfig.class);

    private static AdminConfig instance;

    public static AdminConfig getInstance() {
        return instance;
    }

    @Value("${snail.job.access-token}")
    private String accessToken;

    public String getAccessToken() {
        return accessToken;
    }

    // ---------------------- XxlJobScheduler ----------------------

    private SnailJobScheduler snailJobScheduler;

    @Override
    public void afterPropertiesSet() {
        instance = this;

        // 启动所有守护线程
        snailJobScheduler = new SnailJobScheduler();
//        snailJobScheduler.startAll();
    }

    @Override
    public void destroy() {
        // 停止所有守护线程
//        snailJobScheduler.stopAll();
    }

    // ---------------------- mapper ----------------------

    @Resource
    private JobExecutorMapper jobExecutorMapper;
    @Resource
    protected JobGroupMapper jobGroupMapper;
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
    @Resource
    private DataSource dataSource;

    public JobExecutorMapper getJobExecutorMapper() {
        return jobExecutorMapper;
    }

    public JobGroupMapper getJobGroupMapper() {
        return jobGroupMapper;
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

    public DataSource getDataSource() {
        return dataSource;
    }
}
