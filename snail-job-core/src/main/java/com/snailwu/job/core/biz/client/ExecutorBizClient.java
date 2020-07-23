package com.snailwu.job.core.biz.client;

import com.snailwu.job.core.biz.ExecutorBiz;
import com.snailwu.job.core.biz.model.IdleBeatParam;
import com.snailwu.job.core.biz.model.KillParam;
import com.snailwu.job.core.biz.model.ResultT;
import com.snailwu.job.core.biz.model.TriggerParam;
import com.snailwu.job.core.utils.JobHttpUtil;

/**
 * @author 吴庆龙
 * @date 2020/6/18 11:46 上午
 */
public class ExecutorBizClient implements ExecutorBiz {

    private final String address;

    /**
     * AccessToken
     */
    private final String accessToken;

    /**
     * 超时时间,秒
     */
    private final int timeout = 3;

    public ExecutorBizClient(String address, String accessToken) {
        if (!address.endsWith("/")) {
            address = address + "/";
        }
        this.address = address;

        this.accessToken = accessToken;
    }

    @Override
    public ResultT<String> beat() {
        return JobHttpUtil.postBody(address + "beat", accessToken, null, timeout, String.class);
    }

    @Override
    public ResultT<String> idleBeat(IdleBeatParam idleBeatParam) {
        return JobHttpUtil.postBody(address + "ideaBeat", accessToken, idleBeatParam, timeout, String.class);
    }

    @Override
    public ResultT<String> run(TriggerParam triggerParam) {
        return JobHttpUtil.postBody(address + "run", accessToken, triggerParam, timeout, String.class);
    }

    @Override
    public ResultT<String> kill(KillParam killParam) {
        return JobHttpUtil.postBody(address + "kill", accessToken, killParam, timeout, String.class);
    }

}
