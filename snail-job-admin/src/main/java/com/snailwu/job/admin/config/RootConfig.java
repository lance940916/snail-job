package com.snailwu.job.admin.config;

import com.github.pagehelper.PageInterceptor;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.util.Optional;
import java.util.Properties;

/**
 * @author 吴庆龙
 * @date 2020/5/22 1:41 下午
 */
@Configuration
@Import({
        PropConfig.class
})
@ComponentScan({
        "com.snailwu.job.admin.service",
        "com.snailwu.job.admin.core.alarm",
        "com.snailwu.job.admin.core.conf",
})
@MapperScan("com.snailwu.job.admin.mapper")
public class RootConfig {

    @Resource
    private PropConfig prop;

    /* ---------- 数据源 ---------- */
    @Bean
    public DataSource dataSource() {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(prop.getRequiredEnv("hikari.url"));
        dataSource.setUsername(prop.getRequiredEnv("hikari.username"));
        dataSource.setPassword(prop.getRequiredEnv("hikari.password"));
        dataSource.setDriverClassName(prop.getRequiredEnv("hikari.driver-class-name"));
        dataSource.setConnectionTimeout(prop.getRequiredEnv("hikari.connection-timeout", Long.class));
        dataSource.setIdleTimeout(prop.getRequiredEnv("hikari.idle-timeout", Long.class));
        dataSource.setConnectionTestQuery(prop.getRequiredEnv("hikari.connection-test-query"));
        dataSource.setMinimumIdle(prop.getRequiredEnv("hikari.minimum-idle", Integer.class));
        dataSource.setMaximumPoolSize(prop.getRequiredEnv("hikari.maximum-pool-size", Integer.class));
        dataSource.setPoolName(prop.getRequiredEnv("hikari.pool-name"));
        dataSource.setMaxLifetime(prop.getRequiredEnv("hikari.max-life-time", Long.class));
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

        // 增加分页拦截器
        mbConfig.addInterceptor(pageInterceptor());

        // 	允许 JDBC 支持自动生成主键，需要数据库驱动支持。如果设置为 true，将强制使用自动生成主键。
        // 	尽管一些数据库驱动不支持此特性，但仍可正常工作（如 Derby）。
        mbConfig.setUseGeneratedKeys(true);

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

    /**
     * JavaMailSender
     */
    private static final String DEFAULT_PROTOCOL = "smtp";

    @Bean
    public JavaMailSender javaMailSender() {
        JavaMailSenderImpl sender = new JavaMailSenderImpl();
        sender.setHost(prop.getEnv("alarm.mail.host"));
        sender.setPort(prop.getEnv("alarm.mail.port", Integer.class));
        sender.setUsername(prop.getEnv("alarm.mail.username"));
        sender.setPassword(prop.getEnv("alarm.mail.password"));
        String protocol = Optional.ofNullable(prop.getEnv("alarm.mail.protocol"))
                .orElse(DEFAULT_PROTOCOL);
        sender.setProtocol(protocol);
        return sender;
    }

    public static void main(String[] args) {
        JavaMailSenderImpl sender = new JavaMailSenderImpl();
        sender.setHost("smtp.qiye.aliyun.com");
        sender.setPort(465);
        sender.setUsername("mattermost@zerosportsai.com");
        sender.setPassword("Zerosports2019");
        sender.setProtocol(DEFAULT_PROTOCOL);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("mattermost@zerosportsai.com");
        message.setTo("snail.wu@foxmail.com");
        message.setSubject("Hello Test");
        message.setText("Hello Body");
        sender.send(message);
    }
}
