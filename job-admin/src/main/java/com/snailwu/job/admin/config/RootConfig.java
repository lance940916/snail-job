package com.snailwu.job.admin.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import javax.annotation.Resource;

/**
 * @author 吴庆龙
 * @date 2020/5/22 1:41 下午
 */
@Configuration
public class RootConfig {

    @Resource
    private Environment env;

    @Bean
    public String hell() {
        return "";
    }

}
