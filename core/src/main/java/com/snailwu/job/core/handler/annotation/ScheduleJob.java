package com.snailwu.job.core.handler.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 任务注解
 *
 * @author 吴庆龙
 * @date 2020/5/26 11:32 上午
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ScheduleJob {

    /**
     * 任务名
     */
    String name();

    /**
     * 任务的初始化方法
     * 要求是本类中的方法名
     */
    String init() default "";

    /**
     * 任务的销毁方法
     * 要求是本类中的方法名
     */
    String destroy() default "";

}
