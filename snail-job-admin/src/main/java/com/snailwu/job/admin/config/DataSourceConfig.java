package com.snailwu.job.admin.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

/**
 * @author 吴庆龙
 * @date 2020/5/22 1:41 下午
 */
@Configuration
@PropertySource("classpath:application.properties")
public class DataSourceConfig {

    @Value("${hikari.jdbc-url}")
    private String jdbcUrl;
    @Value("${hikari.driver-class-name}")
    private String driverClassName;
    @Value("${hikari.username}")
    private String username;
    @Value("${hikari.password}")
    private String password;
    @Value("${hikari.connection-timeout}")
    private Long connectionTimeout;
    @Value("${hikari.idle-timeout}")
    private Long idleTimeout;
    @Value("${hikari.minimum-idle}")
    private Integer minimumIdle;
    @Value("${hikari.maximum-pool-size}")
    private Integer maximumPoolSize;
    @Value("${hikari.max-life-time}")
    private Long maxLifeTime;

    /**
     * 数据库连接池配置
     */
    @Bean
    public DataSource dataSource() {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(jdbcUrl);
        dataSource.setDriverClassName(driverClassName);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        // 从连接池中获取连接的超时时间
        dataSource.setConnectionTimeout(connectionTimeout);
        // 大于 minimumIdle 数的连接存活时间
        dataSource.setIdleTimeout(idleTimeout);
        dataSource.setMinimumIdle(minimumIdle);
        dataSource.setMaximumPoolSize(maximumPoolSize);
        dataSource.setMaxLifetime(maxLifeTime);
        return dataSource;
    }

    /**
     * JDBC 事务管理器
     */
    @Bean
    public DataSourceTransactionManager transactionManager() {
        return new DataSourceTransactionManager(dataSource());
    }

}
