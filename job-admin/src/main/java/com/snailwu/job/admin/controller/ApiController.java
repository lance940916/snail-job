package com.snailwu.job.admin.controller;

import com.snailwu.job.core.biz.AdminBiz;
import com.snailwu.job.core.biz.model.CallbackParam;
import com.snailwu.job.core.biz.model.RegistryParam;
import com.snailwu.job.core.biz.model.ResultT;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 接收 Worker 节点的请求
 *
 * @author 吴庆龙
 * @date 2020/6/3 11:31 上午
 */
@RestController
@RequestMapping("/api")
public class ApiController {

    @Resource
    private AdminBiz adminBiz;

    @PostMapping("/callback")
    public ResultT<String> callback(@RequestBody CallbackParam callbackParam) {
        return adminBiz.callback(callbackParam);
    }

    @PostMapping("/registry")
    public ResultT<String> registry(@RequestBody RegistryParam registryParam) {
        return adminBiz.registry(registryParam);
    }

    @PostMapping("/registryRemove")
    public ResultT<String> registryRemove(@RequestBody RegistryParam registryParam) {
        return adminBiz.remove(registryParam);
    }

}
