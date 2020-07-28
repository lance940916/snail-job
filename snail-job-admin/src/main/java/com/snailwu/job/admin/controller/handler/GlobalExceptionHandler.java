package com.snailwu.job.admin.controller.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;

/**
 * Controller 发生 Exception 异常时，进行捕捉并返回执行异常信息
 *
 * @author 吴庆龙
 * @date 2020/7/25 8:17
 */
@ControllerAdvice(annotations = Controller.class)
public class GlobalExceptionHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    public String handler(Exception e, HttpServletRequest request, Model model) {
        // 请求路径
        String uri = request.getRequestURI();
        // 异常信息
        String message = e.getMessage();
        LOGGER.error("[SnailJob]-请求:[{}]发生异常:{}", uri, message);

        model.addAttribute("errorMsg", e.getMessage());
        return "500";
    }

}
