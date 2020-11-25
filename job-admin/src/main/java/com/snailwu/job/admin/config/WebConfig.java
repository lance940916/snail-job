package com.snailwu.job.admin.config;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author 吴庆龙
 * @date 2020/5/22 1:41 下午
 */
@Configuration
@EnableWebMvc
//@ComponentScan(basePackages = "com.snailwu.job.admin.controller")
// 要想对Controller方法做切入，需要在WebConfig中加@EnableAspectJAutoProxy
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class WebConfig implements WebMvcConfigurer, ApplicationContextAware {

    private ApplicationContext applicationContext;

//    @Resource
//    private RequestIdInterceptor requestIdInterceptor;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

//    @Override
//    public void addViewControllers(ViewControllerRegistry registry) {
//        registry.addViewController("/").setViewName("index");
//        registry.addViewController("/group_page").setViewName("group/group.index");
//        registry.addViewController("/info_page").setViewName("info/info.index");
//        registry.addViewController("/log_page").setViewName("log/log.index");
//    }
//
//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(requestIdInterceptor).addPathPatterns("/**");
//    }
//
//    @Override
//    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
//        converters.add(new ByteArrayHttpMessageConverter());
//        converters.add(new StringHttpMessageConverter());
//        converters.add(new AllEncompassingFormHttpMessageConverter());
//
//        ObjectMapper objectMapper = Jackson2ObjectMapperBuilder.json()
//                .applicationContext(applicationContext)
//                .simpleDateFormat("yyyy-MM-dd HH:mm:ss") // 默认使用序列化/反序列化为时间戳
//                .failOnUnknownProperties(false)
//                .serializationInclusion(JsonInclude.Include.NON_NULL)
//                .propertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE)
//                .build();
//        converters.add(new MappingJackson2HttpMessageConverter(objectMapper));
//    }
//
//    /**
//     * 静态资源
//     */
//    @Override
//    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        registry.addResourceHandler("/static/**")
//                .addResourceLocations("classpath:/static/")
//                .setCacheControl(CacheControl.maxAge(Duration.ofDays(1)));
//    }
//
//    /**
//     * 配置 FreeMarker VideoResolver
//     */
//    @Bean
//    public FreeMarkerViewResolver viewResolver() {
//        FreeMarkerViewResolver viewResolver = new FreeMarkerViewResolver();
//        viewResolver.setApplicationContext(applicationContext);
//        viewResolver.setContentType("text/html;charset=UTF-8");
//        viewResolver.setSuffix(".ftl");
//        viewResolver.setCache(false);
//        return viewResolver;
//    }
//
//    /**
//     * 配置 FreeMarker
//     */
//    @Bean
//    public FreeMarkerConfigurer freemarkerConfig() {
//        FreeMarkerConfigurer configurer = new FreeMarkerConfigurer();
//        configurer.setTemplateLoaderPath("classpath:/templates/");
//        configurer.setDefaultEncoding(UTF_8.name());
//        return configurer;
//    }
//
//    @Override
//    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
//        configurer.enable();
//    }
}
