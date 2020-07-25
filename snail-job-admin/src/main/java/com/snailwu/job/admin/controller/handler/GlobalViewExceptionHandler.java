package com.snailwu.job.admin.controller.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;

/**
 * @author 吴庆龙
 * @date 2020/7/25 8:17
 */
@ControllerAdvice
public class GlobalViewExceptionHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalViewExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    public String handler(Exception e, HttpServletRequest request) {
        // 请求路径
        String uri = request.getRequestURI();
        // 异常信息
        String message = e.getMessage();
        LOGGER.error("请求:[{}]发生异常:{}", uri, message);
        return "500";
    }

}
