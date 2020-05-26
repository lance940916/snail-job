package com.snailwu.job.core.biz.client;

import com.fasterxml.jackson.core.type.TypeReference;
import com.snailwu.job.core.biz.AdminBiz;
import com.snailwu.job.core.biz.model.CallbackParam;
import com.snailwu.job.core.biz.model.RegistryParam;
import com.snailwu.job.core.biz.model.ResultT;
import com.snailwu.job.core.utils.HttpUtil;
import com.snailwu.job.core.utils.JsonUtil;

import java.util.List;

/**
 * @author 吴庆龙
 * @date 2020/5/25 2:50 下午
 */
public class AdminBizClient implements AdminBiz {

    private String adminAddress;

    public AdminBizClient() {
    }

    public AdminBizClient(String adminAddress) {
        if (!adminAddress.endsWith("/")) {
            adminAddress = adminAddress + "/";
        }
        this.adminAddress = adminAddress;
    }

    @Override
    public ResultT<String> callback(List<CallbackParam> callbackParamList) {
        String content = JsonUtil.writeValueAsString(callbackParamList);
        String result = HttpUtil.post(adminAddress + "api/callback", content);
        return JsonUtil.readValue(result, new TypeReference<ResultT<String>>() {
        });
    }

    @Override
    public ResultT<String> registry(RegistryParam registryParam) {
        String content = JsonUtil.writeValueAsString(registryParam);
        String result = HttpUtil.post(adminAddress + "api/registry", content);
        return JsonUtil.readValue(result, new TypeReference<ResultT<String>>() {
        });
    }

    @Override
    public ResultT<String> registryRemove(RegistryParam registryParam) {
        String content = JsonUtil.writeValueAsString(registryParam);
        String result = HttpUtil.post(adminAddress + "api/registryRemove", content);
        return JsonUtil.readValue(result, new TypeReference<ResultT<String>>() {
        });
    }
}
