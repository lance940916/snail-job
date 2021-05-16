package com.snailwu.job.core.biz.client;

import com.fasterxml.jackson.core.type.TypeReference;
import com.snailwu.job.core.biz.ExecutorBiz;
import com.snailwu.job.core.biz.model.IdleBeatParam;
import com.snailwu.job.core.biz.model.KillParam;
import com.snailwu.job.core.biz.model.ResultT;
import com.snailwu.job.core.biz.model.TriggerParam;
import com.snailwu.job.core.utils.JobHttpUtil;
import com.snailwu.job.core.utils.JobJsonUtil;

import static com.snailwu.job.core.constants.CoreConstant.URL_SEPARATOR;

/**
 * Admin 调用 Core 接口的 Client
 *
 * @author 吴庆龙
 * @date 2020/6/18 11:46 上午
 */
public class ExecutorBizClient implements ExecutorBiz {

    private final String address;
    private final String accessToken;

    public ExecutorBizClient(String address, String accessToken) {
        if (!address.endsWith(URL_SEPARATOR)) {
            address = address + URL_SEPARATOR;
        }
        this.address = address;
        this.accessToken = accessToken;
    }

    @Override
    public ResultT<String> beat() {
        String respContent = JobHttpUtil.post(address + "beat", accessToken, null);
        return JobJsonUtil.readValue(respContent, new TypeReference<ResultT<String>>() {
        });
    }

    @Override
    public ResultT<String> idleBeat(IdleBeatParam idleBeatParam) {
        String respContent = JobHttpUtil.post(address + "ideaBeat", accessToken, idleBeatParam);
        return JobJsonUtil.readValue(respContent, new TypeReference<ResultT<String>>() {
        });
    }

    @Override
    public ResultT<String> run(TriggerParam triggerParam) {
        String respContent = JobHttpUtil.post(address + "run", accessToken, triggerParam);
        return JobJsonUtil.readValue(respContent, new TypeReference<ResultT<String>>() {
        });
    }

    @Override
    public ResultT<String> kill(KillParam killParam) {
        String respContent = JobHttpUtil.post(address + "kill", accessToken, killParam);
        return JobJsonUtil.readValue(respContent, new TypeReference<ResultT<String>>() {
        });
    }

}
