package com.snailwu.job.admin.controller;

import com.snailwu.job.core.biz.AdminBiz;
import com.snailwu.job.core.biz.model.CallbackParam;
import com.snailwu.job.core.biz.model.RegistryParam;
import com.snailwu.job.core.biz.model.ResultT;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

import static com.snailwu.job.core.utils.HttpUtil.SNAIL_JOB_ACCESS_TOKEN;

/**
 * 接收 Worker 节点的请求
 *
 * @author 吴庆龙
 * @date 2020/6/3 11:31 上午
 */
@RestController
@RequestMapping("/snail/job/api")
public class JobApiController {
    @Resource
    private AdminBiz adminBiz;

    private String adminAccessToken;

    @PostMapping("/callback")
    public ResultT<String> callback(
            @RequestHeader(name = SNAIL_JOB_ACCESS_TOKEN, required = false) String clientAccessToken,
            @RequestBody List<CallbackParam> callbackParamList) {
        if (adminAccessToken.equals(clientAccessToken)) {
            return new ResultT<>(ResultT.FAIL_CODE, "AccessToken错误");
        }
        return adminBiz.callback(callbackParamList);
    }

    @PostMapping("/registry")
    public ResultT<String> registry(
            @RequestHeader(name = SNAIL_JOB_ACCESS_TOKEN, required = false) String clientAccessToken,
            @RequestBody RegistryParam registryParam) {
        if (adminAccessToken.equals(clientAccessToken)) {
            return new ResultT<>(ResultT.FAIL_CODE, "AccessToken错误");
        }
        return adminBiz.registry(registryParam);
    }

    @PostMapping("/registryRemove")
    public ResultT<String> registryRemove(
            @RequestHeader(name = SNAIL_JOB_ACCESS_TOKEN, required = false) String clientAccessToken,
            @RequestBody RegistryParam registryParam) {
        if (adminAccessToken.equals(clientAccessToken)) {
            return new ResultT<>(ResultT.FAIL_CODE, "AccessToken错误");
        }
        return adminBiz.registryRemove(registryParam);
    }
}
