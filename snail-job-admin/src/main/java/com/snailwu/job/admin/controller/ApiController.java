package com.snailwu.job.admin.controller;

import com.snailwu.job.core.biz.AdminBiz;
import com.snailwu.job.core.biz.model.CallbackParam;
import com.snailwu.job.core.biz.model.RegistryParam;
import com.snailwu.job.core.biz.model.ResultT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 接收 Worker 节点的请求
 *
 * @author 吴庆龙
 * @date 2020/6/3 11:31 上午
 */
@RestController
@RequestMapping("/api")
public class ApiController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ApiController.class);

    @Resource
    private AdminBiz adminBiz;

    @PostMapping("/callback")
    public ResultT<String> callback(@RequestBody List<CallbackParam> callbackParamList) {
        return adminBiz.callback(callbackParamList);
    }

    @PostMapping("/registry")
    public ResultT<String> registry(@RequestBody RegistryParam registryParam) {
        return adminBiz.registry(registryParam);
    }

    @PostMapping("/registryRemove")
    public ResultT<String> registryRemove(@RequestBody RegistryParam registryParam) {
        return adminBiz.registryRemove(registryParam);
    }

    /**
     * 针对 Api Restful 接口进行的异常封装处理
     */
    @ExceptionHandler(Exception.class)
    public ResultT<String> apiExceptionHandler(Exception e, HttpServletRequest request) {
        // 请求路径
        String uri = request.getRequestURI();
        // 异常信息
        String message = e.getMessage();
        LOGGER.error("请求:[{}]发生异常:{}", uri, message);
        return new ResultT<>(ResultT.FAIL_CODE, e.getMessage());
    }
}
