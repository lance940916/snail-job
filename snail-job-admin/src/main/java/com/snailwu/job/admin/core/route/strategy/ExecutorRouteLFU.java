package com.snailwu.job.admin.core.route.strategy;

import com.snailwu.job.admin.core.route.ExecutorRouter;
import com.snailwu.job.core.biz.model.ResultT;
import com.snailwu.job.core.biz.model.TriggerParam;

import java.util.List;

/**
 * @author 吴庆龙
 * @date 2020/6/17 10:10 上午
 */
public class ExecutorRouteLFU extends ExecutorRouter {
    @Override
    public ResultT<String> route(TriggerParam triggerParam, List<String> addressList) {
        return null;
    }
}
