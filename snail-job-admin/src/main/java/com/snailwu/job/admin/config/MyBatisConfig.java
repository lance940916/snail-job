package com.snailwu.job.admin.config;

import com.github.pagehelper.PageInterceptor;
import org.apache.ibatis.logging.log4j2.Log4j2Impl;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.util.Properties;

/**
 * @author 吴庆龙
 * @date 2020/11/25 下午2:44
 */
@Configuration
@MapperScan("com.snailwu.job.admin.mapper")
public class MyBatisConfig {

    @Resource
    private DataSource dataSource;

    /**
     * 配置属性
     */
    @Bean
    public org.apache.ibatis.session.Configuration myBatisConfiguration() {
        org.apache.ibatis.session.Configuration mbConfig = new org.apache.ibatis.session.Configuration();

        // 增加分页拦截器
        mbConfig.addInterceptor(pageInterceptor());

        // 	允许 JDBC 支持自动生成主键，需要数据库驱动支持。如果设置为 true，将强制使用自动生成主键
        // 	尽管一些数据库驱动不支持此特性，但仍可正常工作（如 Derby）
        mbConfig.setUseGeneratedKeys(true);

        // 日志配置
        mbConfig.setLogImpl(Log4j2Impl.class);
        mbConfig.setLogPrefix("mybatis-");

        // 自动转驼峰命名
        mbConfig.setMapUnderscoreToCamelCase(true);

        return mbConfig;
    }

    /**
     * 配置 SqlSessionFactory
     */
    @Bean
    public SqlSessionFactory sqlSessionFactory() throws Exception {
        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();

        // 数据源
        factoryBean.setDataSource(dataSource);

        // MyBatis JavaConfig配置
        factoryBean.setConfiguration(myBatisConfiguration());

        return factoryBean.getObject();
    }

    /**
     * 分页配置
     */
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
