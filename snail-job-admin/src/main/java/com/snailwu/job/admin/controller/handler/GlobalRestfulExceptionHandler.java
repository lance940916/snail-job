package com.snailwu.job.admin.controller.handler;

import com.snailwu.job.core.biz.model.ResultT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

/**
 * Controller 发生 Exception 异常时，进行捕捉并返回执行异常信息
 *
 * @author 吴庆龙
 * @date 2020/7/25 8:17
 */
@RestControllerAdvice(annotations = RestController.class)
public class GlobalRestfulExceptionHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalRestfulExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    public ResultT<String> handler(Exception e, HttpServletRequest request) {
        // 请求路径
        String uri = request.getRequestURI();
        // 异常信息
        String message = e.getMessage();
        LOGGER.error("[SnailJob]-Rest请求:[{}]发生异常:{}", uri, message);
        return new ResultT<>(ResultT.FAIL_CODE, e.getMessage());
    }

}
