package com.snailwu.job.admin.controller.aspect;

import com.snailwu.job.core.biz.model.ResultT;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

import static com.snailwu.job.core.constants.JobCoreConstant.JOB_ACCESS_TOKEN;

/**
 * 校验AccessToken的切面
 * <p>
 * 校验 Client 的 AccessToken 是否正确
 *
 * @author 吴庆龙
 * @date 2020/7/23 10:41 上午
 */
@Aspect
@Component
@PropertySource("classpath:application.properties")
public class ApiAccessTokenAspect {
    private static final Logger LOGGER = LoggerFactory.getLogger(ApiAccessTokenAspect.class);

    @Value("${job-access-token}")
    private String accessToken;

    @Around("execution(* com.snailwu.job.admin.controller.ApiController.*(..))")
    public Object beforeAccess(ProceedingJoinPoint joinPoint) throws Throwable {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes)
                RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();
        String clientAccessToken = request.getHeader(JOB_ACCESS_TOKEN);
        if (accessToken.equals(clientAccessToken)) {
            return joinPoint.proceed();
        }
        String uri = request.getRequestURI();
        LOGGER.error("AccessToken错误: {}", uri);
        return new ResultT<>(ResultT.FAIL_CODE, "访问受限");
    }

}
