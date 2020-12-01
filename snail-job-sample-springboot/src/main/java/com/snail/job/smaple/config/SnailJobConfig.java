package com.snail.job.smaple.config;

import com.snailwu.job.core.node.SnailJobNodeProperties;
import com.snailwu.job.core.node.impl.SnailJobSpringNode;
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
    public SnailJobSpringNode snailJobSpringExecutor() {
        SnailJobNodeProperties properties = new SnailJobNodeProperties();
        properties.setAdminAddress("http://localhost:8080/snail-job-admin");
        properties.setAppName("default-group");
        properties.setHostIp("127.0.0.1");
        properties.setHostPort(9999);
        properties.setAccessToken("hello-job");

        return new SnailJobSpringNode(properties);
    }

}
