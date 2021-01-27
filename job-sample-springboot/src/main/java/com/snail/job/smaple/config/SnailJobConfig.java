package com.snail.job.smaple.config;

import com.snailwu.job.core.executor.impl.JobSpringExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author 吴庆龙
 * @date 2020/8/11 10:21 上午
 */
@Configuration
public class SnailJobConfig {
    private static final Logger LOGGER = LoggerFactory.getLogger(SnailJobConfig.class);

    @Bean
    public JobSpringExecutor snailJobSpringExecutor() {
        JobSpringExecutor executor = new JobSpringExecutor();
        executor.setAdminAddress("http://localhost:8080/snail-job-admin");
        executor.setAppName("default-group");
        executor.setIp("127.0.0.1");
        executor.setPort(7479);
        executor.setAccessToken("c16fb6b0-8c69-40da-a00f-cb54fc05ea53");
        return executor;
    }

}
