package com.snailwu.job.admin.controller.handler;

import com.snailwu.job.core.biz.model.ResultT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import java.util.Iterator;
import java.util.List;

/**
 * Controller 发生 Exception 异常时，进行捕捉并返回执行异常信息
 *
 * @author 吴庆龙
 * @date 2020/7/25 8:17
 */
@RestControllerAdvice(annotations = RestController.class)
public class GlobalRestExceptionHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalRestExceptionHandler.class);

    /**
     * 返回 Validate 校验错误信息
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResultT<String> validHandler(MethodArgumentNotValidException e) {
        StringBuilder sb = new StringBuilder();

        BindingResult bindingResult = e.getBindingResult();
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        Iterator<FieldError> iter = fieldErrors.iterator();
        FieldError error = iter.next();
        sb.append(error.getField()).append(":").append(error.getDefaultMessage());
        while (iter.hasNext()) {
            sb.append(";");
            sb.append(error.getField());
            sb.append(": ");
            sb.append(error.getDefaultMessage());
        }
        return new ResultT<>(ResultT.FAIL_CODE, sb.toString());
    }

    @ExceptionHandler(Exception.class)
    public ResultT<String> handler(Exception e, HttpServletRequest request) {
        // 请求路径
        String uri = request.getRequestURI();
        // 异常信息
        String message = e.getMessage();
        LOGGER.error("Rest请求:[{}]发生异常:{}", uri, message);
        return new ResultT<>(ResultT.FAIL_CODE, e.getMessage());
    }

}
