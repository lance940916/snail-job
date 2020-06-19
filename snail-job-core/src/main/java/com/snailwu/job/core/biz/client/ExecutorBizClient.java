package com.snailwu.job.core.biz.client;

import com.fasterxml.jackson.core.type.TypeReference;
import com.snailwu.job.core.biz.ExecutorBiz;
import com.snailwu.job.core.biz.model.*;
import com.snailwu.job.core.utils.HttpUtil;
import com.snailwu.job.core.utils.JsonUtil;

/**
 * @author 吴庆龙
 * @date 2020/6/18 11:46 上午
 */
public class ExecutorBizClient implements ExecutorBiz {

    private final String address;

    public ExecutorBizClient(String address) {
        if (!address.endsWith("/")) {
            address = address + "/";
        }
        this.address = address;
    }

    @Override
    public ResultT<String> beat() {
        String result = HttpUtil.post(address + "beat", "");
        return JsonUtil.readValue(result, new TypeReference<ResultT<String>>() {
        });
    }

    @Override
    public ResultT<String> idleBeat(IdleBeatParam idleBeatParam) {
        String content = JsonUtil.writeValueAsString(idleBeatParam);
        String result = HttpUtil.post(address + "ideaBeat", content);
        return JsonUtil.readValue(result, new TypeReference<ResultT<String>>() {
        });
    }

    @Override
    public ResultT<String> run(TriggerParam triggerParam) {
        String content = JsonUtil.writeValueAsString(triggerParam);
        String result = HttpUtil.post(address + "run", content);
        return JsonUtil.readValue(result, new TypeReference<ResultT<String>>() {
        });
    }

    @Override
    public ResultT<String> kill(KillParam killParam) {
        String content = JsonUtil.writeValueAsString(killParam);
        String result = HttpUtil.post(address + "kill", content);
        return JsonUtil.readValue(result, new TypeReference<ResultT<String>>() {
        });
    }

}
