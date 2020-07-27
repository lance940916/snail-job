package com.snailwu.job.admin.config;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

import javax.annotation.Resource;

/**
 * @author 吴庆龙
 * @date 2020/7/27 7:28
 */
@PropertySource("classpath:application.properties")
public class PropConfig implements InitializingBean {

    @Resource
    private Environment env;

    private static PropConfig instance;

    public static PropConfig getInstance() {
        return instance;
    }

    public String getEnv(String key) {
        return env.getProperty(key);
    }

    public <T> T getEnv(String key, Class<T> targetType) {
        return env.getProperty(key, targetType);
    }

    public String getRequiredEnv(String key) {
        return env.getRequiredProperty(key);
    }

    public <T> T getRequiredEnv(String key, Class<T> targetType) {
        return env.getRequiredProperty(key, targetType);
    }

    @Override
    public void afterPropertiesSet() {
        PropConfig.instance = this;
    }
}
