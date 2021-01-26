package com.snailwu.job.admin.core.route.strategy;

import com.snailwu.job.admin.core.route.ExecRouter;
import com.snailwu.job.core.biz.model.ResultT;
import com.snailwu.job.core.biz.model.TriggerParam;

/**
 * 第一个执行
 *
 * @author 吴庆龙
 * @date 2020/6/17 10:11 上午
 */
public class ExecRouteFirst extends ExecRouter {
    @Override
    public ResultT<String> route(TriggerParam triggerParam, String[] addresses) {
        if (addresses.length == 0) {
            return new ResultT<>(ResultT.FAIL_CODE, NO_FOUND_ADDRESS_MSG);
        }
        return new ResultT<>(addresses[0]);
    }
}
