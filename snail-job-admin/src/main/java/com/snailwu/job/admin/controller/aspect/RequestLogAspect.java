package com.snailwu.job.admin.controller.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author 吴庆龙
 * @date 2020/8/12 12:40 下午
 */
//@Aspect
//@Component
public class RequestLogAspect {
    private static final Logger LOGGER = LoggerFactory.getLogger(RequestLogAspect.class);

//    @Around("execution(* com.snailwu.job.admin.controller..*(..)))")
    public Object around(ProceedingJoinPoint pjp) {
        // 1. 打印请求参数
        Object[] args = pjp.getArgs();
        for (Object arg : args) {
            System.out.println(arg);
        }

        // 2. 执行目标方法
        Object resultObj = null;
        try {
            resultObj = pjp.proceed();
        } catch (Throwable throwable) {
            // 3. 处理异常
            throwable.printStackTrace();
        }

        // 4. 打印返回值

        return resultObj;
    }

}
