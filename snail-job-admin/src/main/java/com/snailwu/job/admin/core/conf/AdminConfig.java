package com.snailwu.job.admin.core.conf;

import com.snailwu.job.admin.core.scheduler.SnailJobScheduler;
import com.snailwu.job.admin.mapper.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author 吴庆龙
 * @date 2020/6/8 1:49 下午
 */
@Component
public class AdminConfig implements InitializingBean, DisposableBean {
    private static final Logger log = LoggerFactory.getLogger(AdminConfig.class);

    private static AdminConfig instance;

    public static AdminConfig getInstance() {
        return instance;
    }

    // ---------------------- AdminProperties ----------------------

    @Resource
    private AdminProperties properties;

    public AdminProperties getProperties() {
        return properties;
    }

    // ---------------------- XxlJobScheduler ----------------------

    private SnailJobScheduler snailJobScheduler;

    @Override
    public void afterPropertiesSet() throws Exception {
        instance = this;

        snailJobScheduler= new SnailJobScheduler();
        snailJobScheduler.init();
    }

    @Override
    public void destroy() throws Exception {
        snailJobScheduler.destroy();
    }

    // ---------------------- dao service ----------------------

    @Resource
    private JobExecutorMapper jobExecutorMapper;
    @Resource
    protected JobExecutorNodeMapper jobExecutorNodeMapper;
    @Resource
    private JobInfoMapper jobInfoMapper;
    @Resource
    private JobLockMapper jobLockMapper;
    @Resource
    private JobLogMapper jobLogMapper;
    @Resource
    private JobLogReportMapper jobLogReportMapper;

    public JobExecutorMapper getJobExecutorMapper() {
        return jobExecutorMapper;
    }

    public JobExecutorNodeMapper getJobExecutorNodeMapper() {
        return jobExecutorNodeMapper;
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
}
