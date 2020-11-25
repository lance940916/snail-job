package com.snailwu.job.admin.config;

/**
 * @author 吴庆龙
 * @date 2020/5/22 1:41 下午
 */
//@Configuration
//@Import({
//        PropConfig.class
//})
//@ComponentScan({
//        "com.snailwu.job.admin.service",
//        "com.snailwu.job.admin.core.alarm",
//        "com.snailwu.job.admin.core.conf",
//})
//@MapperScan("com.snailwu.job.admin.mapper")
public class DataSourceConfig {

//    @Resource
//    private PropConfig prop;
//
//    /* ---------- 数据源 ---------- */
//    @Bean
//    public DataSource dataSource() {
//        HikariDataSource dataSource = new HikariDataSource();
//        dataSource.setJdbcUrl(prop.getRequiredEnv("hikari.url"));
//        dataSource.setUsername(prop.getRequiredEnv("hikari.username"));
//        dataSource.setPassword(prop.getRequiredEnv("hikari.password"));
//        dataSource.setDriverClassName(prop.getRequiredEnv("hikari.driver-class-name"));
//        dataSource.setConnectionTimeout(prop.getRequiredEnv("hikari.connection-timeout", Long.class));
//        dataSource.setIdleTimeout(prop.getRequiredEnv("hikari.idle-timeout", Long.class));
//        dataSource.setConnectionTestQuery(prop.getRequiredEnv("hikari.connection-test-query"));
//        dataSource.setMinimumIdle(prop.getRequiredEnv("hikari.minimum-idle", Integer.class));
//        dataSource.setMaximumPoolSize(prop.getRequiredEnv("hikari.maximum-pool-size", Integer.class));
//        dataSource.setMaxLifetime(prop.getRequiredEnv("hikari.max-life-time", Long.class));
//        return dataSource;
//    }
//
//    /* ---------- Spring 事务管理器 ---------- */
//    @Bean
//    public DataSourceTransactionManager transactionManager() {
//        return new DataSourceTransactionManager(dataSource());
//    }
//
//    /* ---------- MyBatis 配置 ---------- */
//    @Bean
//    public SqlSessionFactory sqlSessionFactory() throws Exception {
//        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
//
//        // 数据源
//        factoryBean.setDataSource(dataSource());
//
//        // MyBatis JavaConfig配置
//        factoryBean.setConfiguration(myBatisConfiguration());
//
//        return factoryBean.getObject();
//    }
//
//    /**
//     * MyBatis 配置
//     */
//    @Bean
//    public org.apache.ibatis.session.Configuration myBatisConfiguration() {
//        org.apache.ibatis.session.Configuration mbConfig = new org.apache.ibatis.session.Configuration();
//
//        // 增加分页拦截器
////        mbConfig.addInterceptor(pageInterceptor());
//
//        // 	允许 JDBC 支持自动生成主键，需要数据库驱动支持。如果设置为 true，将强制使用自动生成主键。
//        // 	尽管一些数据库驱动不支持此特性，但仍可正常工作（如 Derby）。
//        mbConfig.setUseGeneratedKeys(true);
//
//        return mbConfig;
//    }
//
//    /* ---------- MyBatis 分页配置 ---------- */
//    @Bean
//    public PageInterceptor pageInterceptor() {
//        Properties properties = new Properties();
//        properties.setProperty("helperDialect", "mysql");
//        properties.setProperty("reasonable", "true");
//        PageInterceptor pageInterceptor = new PageInterceptor();
//        pageInterceptor.setProperties(properties);
//        return pageInterceptor;
//    }

}
