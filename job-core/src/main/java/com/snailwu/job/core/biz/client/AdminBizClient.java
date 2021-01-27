package com.snailwu.job.core.biz.client;

import com.fasterxml.jackson.core.type.TypeReference;
import com.snailwu.job.core.biz.AdminBiz;
import com.snailwu.job.core.biz.model.CallbackParam;
import com.snailwu.job.core.biz.model.RegistryParam;
import com.snailwu.job.core.biz.model.ResultT;
import com.snailwu.job.core.utils.JobHttpUtil;
import com.snailwu.job.core.utils.JobJsonUtil;

import static com.snailwu.job.core.constants.CoreConstant.URL_SEPARATOR;

/**
 * Core 调用 Admin 接口的 Client
 *
 * @author 吴庆龙
 * @date 2020/5/25 2:50 下午
 */
public class AdminBizClient implements AdminBiz {

    /**
     * 调度中心的地址
     */
    private final String address;

    /**
     * AccessToken
     */
    private final String accessToken;

    public AdminBizClient(String address, String accessToken) {
        // 追加 / 符号
        if (!address.endsWith(URL_SEPARATOR)) {
            address = address + URL_SEPARATOR;
        }

        this.address = address;
        this.accessToken = accessToken;
    }

    @Override
    public ResultT<String> callback(CallbackParam callbackParam) {
        String respContent = JobHttpUtil.post(address + "api/callback", accessToken, callbackParam);
        return JobJsonUtil.readValue(respContent, new TypeReference<>() {
        });
    }

    @Override
    public ResultT<String> registryNode(RegistryParam registryParam) {
        String respContent = JobHttpUtil.post(address + "api/registry", accessToken, registryParam);
        return JobJsonUtil.readValue(respContent, new TypeReference<>() {
        });
    }

    @Override
    public ResultT<String> removeNode(RegistryParam registryParam) {
        String respContent = JobHttpUtil.post(address + "api/registryRemove", accessToken, registryParam);
        return JobJsonUtil.readValue(respContent, new TypeReference<>() {
        });
    }
}
