package com.snailwu.job.admin.core.conf;

import com.snailwu.job.admin.core.scheduler.SnailJobScheduler;
import com.snailwu.job.admin.dao.SnailJobGroupMapper;
import com.snailwu.job.admin.dao.SnailJobInfoMapper;
import com.snailwu.job.admin.dao.SnailJobLockMapper;
import com.snailwu.job.admin.dao.SnailJobRegistryMapper;
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
public class SnailJobAdminConfig implements InitializingBean, DisposableBean {
    private static final Logger log = LoggerFactory.getLogger(SnailJobAdminConfig.class);

    private static SnailJobAdminConfig instance;

    public static SnailJobAdminConfig getInstance() {
        return instance;
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
    private SnailJobGroupMapper snailJobGroupMapper;
    @Resource
    private SnailJobInfoMapper snailJobInfoMapper;
    @Resource
    private SnailJobLockMapper snailJobLockMapper;
    @Resource
    private SnailJobRegistryMapper snailJobRegistryMapper;

    public SnailJobGroupMapper getSnailJobGroupMapper() {
        return snailJobGroupMapper;
    }

    public SnailJobInfoMapper getSnailJobInfoMapper() {
        return snailJobInfoMapper;
    }

    public SnailJobLockMapper getSnailJobLockMapper() {
        return snailJobLockMapper;
    }

    public SnailJobRegistryMapper getSnailJobRegistryMapper() {
        return snailJobRegistryMapper;
    }
}
