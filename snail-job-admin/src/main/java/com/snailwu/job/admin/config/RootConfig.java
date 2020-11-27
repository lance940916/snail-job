package com.snailwu.job.admin.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;

/**
 * @author 吴庆龙
 * @date 2020/5/22 1:41 下午
 */
@Configuration
@Import({
        DataSourceConfig.class,
        MyBatisConfig.class
})
@ComponentScan(basePackages = {
        "com.snailwu.job.admin.core",
        "com.snailwu.job.admin.service",
})
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class RootConfig {

}
