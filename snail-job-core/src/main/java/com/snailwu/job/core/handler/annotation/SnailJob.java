package com.snailwu.job.core.handler.annotation;

import java.lang.annotation.*;

/**
 * @author 吴庆龙
 * @date 2020/5/26 11:32 上午
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface SnailJob {

    String value();

    String init() default "";

    String destroy() default "";

}
