package com.snailwu.job.admin.controller.handler;

import com.snailwu.job.core.biz.model.ResultT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

/**
 * @author 吴庆龙
 * @date 2020/7/25 8:17
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    public Object handler(Exception e, HttpServletRequest request) {
        // 请求路径
        String uri = request.getRequestURI();
        // 异常信息
        String message = e.getMessage();
        log.error("请求:[{}]发生异常:{}", uri, message);
        return new ResultT<>(ResultT.FAIL_CODE, e.getMessage());
    }

}
