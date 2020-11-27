package com.snailwu.job.admin.config;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
import org.springframework.web.servlet.view.freemarker.FreeMarkerViewResolver;

import java.nio.charset.StandardCharsets;

/**
 * @author 吴庆龙
 * @date 2020/11/26 下午4:58
 */
@Configuration
public class FreeMarkerConfig implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    /**
     * 配置 FreeMarker VideoResolver
     */
    @Bean
    public FreeMarkerViewResolver viewResolver() {
        FreeMarkerViewResolver viewResolver = new FreeMarkerViewResolver();
        viewResolver.setApplicationContext(applicationContext);
        viewResolver.setContentType("text/html;charset=UTF-8");
        viewResolver.setSuffix(".ftl");
        viewResolver.setCache(false);
        return viewResolver;
    }

    /**
     * 配置 FreeMarker
     */
    @Bean
    public FreeMarkerConfigurer freemarkerConfig() {
        FreeMarkerConfigurer configurer = new FreeMarkerConfigurer();
        configurer.setTemplateLoaderPath("classpath:/templates/");
        configurer.setDefaultEncoding(StandardCharsets.UTF_8.name());
        return configurer;
    }

}
