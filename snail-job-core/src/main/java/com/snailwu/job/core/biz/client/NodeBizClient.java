package com.snailwu.job.core.biz.client;

import com.fasterxml.jackson.core.type.TypeReference;
import com.snailwu.job.core.biz.NodeBiz;
import com.snailwu.job.core.biz.model.IdleBeatParam;
import com.snailwu.job.core.biz.model.KillParam;
import com.snailwu.job.core.biz.model.ResultT;
import com.snailwu.job.core.biz.model.TriggerParam;
import com.snailwu.job.core.utils.SnailJobHttpUtil;
import com.snailwu.job.core.utils.SnailJobJsonUtil;

import static com.snailwu.job.core.constants.JobCoreConstant.URL_SEPARATOR;

/**
 * Admin 调用 Core 接口的 Client
 *
 * @author 吴庆龙
 * @date 2020/6/18 11:46 上午
 */
public class NodeBizClient implements NodeBiz {

    private final String address;

    /**
     * AccessToken
     */
    private final String accessToken;

    public NodeBizClient(String address, String accessToken) {
        if (!address.endsWith(URL_SEPARATOR)) {
            address = address + URL_SEPARATOR;
        }
        this.address = address;

        this.accessToken = accessToken;
    }

    @Override
    public ResultT<String> beat() {
        String respContent = SnailJobHttpUtil.post(address + "beat", accessToken, null);
        return SnailJobJsonUtil.readValue(respContent, new TypeReference<ResultT<String>>() {
        });
    }

    @Override
    public ResultT<String> idleBeat(IdleBeatParam idleBeatParam) {
        String respContent = SnailJobHttpUtil.post(address + "ideaBeat", accessToken, idleBeatParam);
        return SnailJobJsonUtil.readValue(respContent, new TypeReference<ResultT<String>>() {
        });
    }

    @Override
    public ResultT<String> run(TriggerParam triggerParam) {
        String respContent = SnailJobHttpUtil.post(address + "run", accessToken, triggerParam);
        return SnailJobJsonUtil.readValue(respContent, new TypeReference<ResultT<String>>() {
        });
    }

    @Override
    public ResultT<String> kill(KillParam killParam) {
        String respContent = SnailJobHttpUtil.post(address + "kill", accessToken, killParam);
        return SnailJobJsonUtil.readValue(respContent, new TypeReference<ResultT<String>>() {
        });
    }

}
