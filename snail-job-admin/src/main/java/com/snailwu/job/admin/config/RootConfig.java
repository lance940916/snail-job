package com.snailwu.job.admin.config;

import com.github.pagehelper.PageInterceptor;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.util.Properties;

/**
 * @author 吴庆龙
 * @date 2020/5/22 1:41 下午
 */
@Configuration
@PropertySource("classpath:db.properties")
@ComponentScan({
        "com.snailwu.job.admin.service",
        "com.snailwu.job.admin.core.alarm",
        "com.snailwu.job.admin.core.conf",
})
@MapperScan("com.snailwu.job.admin.mapper")
public class RootConfig {

    @Resource
    private Environment env;

    /* ---------- 数据源 ---------- */
    @Bean
    public DataSource dataSource() {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(env.getRequiredProperty("hikari.url"));
        dataSource.setUsername(env.getRequiredProperty("hikari.username"));
        dataSource.setPassword(env.getRequiredProperty("hikari.password"));
        dataSource.setDriverClassName(env.getRequiredProperty("hikari.driver-class-name"));
        dataSource.setConnectionTimeout(env.getRequiredProperty("hikari.connection-timeout", Long.class));
        dataSource.setIdleTimeout(env.getRequiredProperty("hikari.idle-timeout", Long.class));
        dataSource.setConnectionTestQuery(env.getRequiredProperty("hikari.connection-test-query"));
        dataSource.setMinimumIdle(env.getRequiredProperty("hikari.minimum-idle", Integer.class));
        dataSource.setMaximumPoolSize(env.getRequiredProperty("hikari.maximum-pool-size", Integer.class));
        dataSource.setPoolName(env.getRequiredProperty("hikari.pool-name"));
        dataSource.setMaxLifetime(env.getRequiredProperty("hikari.max-life-time", Long.class));
        return dataSource;
    }

    /* ---------- Spring 事务管理器 ---------- */
    @Bean
    public DataSourceTransactionManager transactionManager() {
        return new DataSourceTransactionManager(dataSource());
    }

    /* ---------- MyBatis 配置 ---------- */
    @Bean
    public SqlSessionFactory sqlSessionFactory() throws Exception {
        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();

        // 数据源
        factoryBean.setDataSource(dataSource());

        // MyBatis JavaConfig配置
        factoryBean.setConfiguration(myBatisConfiguration());

        return factoryBean.getObject();
    }

    /**
     * MyBatis 配置
     */
    @Bean
    public org.apache.ibatis.session.Configuration myBatisConfiguration() {
        org.apache.ibatis.session.Configuration mbConfig = new org.apache.ibatis.session.Configuration();
        mbConfig.setLogPrefix("mybatis-");

        // 增加分页拦截器
        mbConfig.addInterceptor(pageInterceptor());
        return mbConfig;
    }

    /* ---------- MyBatis 分页配置 ---------- */
    @Bean
    public PageInterceptor pageInterceptor() {
        Properties properties = new Properties();
        properties.setProperty("helperDialect", "mysql");
        properties.setProperty("reasonable", "true");
        PageInterceptor pageInterceptor = new PageInterceptor();
        pageInterceptor.setProperties(properties);
        return pageInterceptor;
    }

}
