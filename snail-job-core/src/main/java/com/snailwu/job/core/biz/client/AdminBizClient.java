package com.snailwu.job.core.biz.client;

import com.snailwu.job.core.biz.AdminBiz;
import com.snailwu.job.core.biz.model.CallbackParam;
import com.snailwu.job.core.biz.model.RegistryParam;
import com.snailwu.job.core.biz.model.ResultT;
import com.snailwu.job.core.utils.JobHttpUtil;

import java.util.List;

/**
 * @author 吴庆龙
 * @date 2020/5/25 2:50 下午
 */
public class AdminBizClient implements AdminBiz {

    /**
     * 调度中心的地址
     */
    private final String adminAddress;

    /**
     * AccessToken
     */
    private final String accessToken;

    /**
     * 超时时间,秒
     */
    private final int timeout = 3;

    public AdminBizClient(String adminAddress, String accessToken) {
        if (!adminAddress.endsWith("/")) {
            adminAddress = adminAddress + "/";
        }
        this.adminAddress = adminAddress;

        this.accessToken = accessToken;
    }

    @Override
    public ResultT<String> callback(List<CallbackParam> callbackParamList) {
        return JobHttpUtil.postBody(adminAddress + "api/callback", accessToken, callbackParamList, timeout, String.class);
    }

    @Override
    public ResultT<String> registry(RegistryParam registryParam) {
        return JobHttpUtil.postBody(adminAddress + "api/registry", accessToken, registryParam, timeout, String.class);
    }

    @Override
    public ResultT<String> registryRemove(RegistryParam registryParam) {
        return JobHttpUtil.postBody(adminAddress + "api/registryRemove", accessToken, registryParam, timeout, String.class);
    }
}
