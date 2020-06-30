package com.snailwu.job.admin.core.route.strategy;

import com.snailwu.job.admin.core.route.ExecutorRouter;
import com.snailwu.job.admin.core.scheduler.SnailJobScheduler;
import com.snailwu.job.core.biz.ExecutorBiz;
import com.snailwu.job.core.biz.model.ResultT;
import com.snailwu.job.core.biz.model.TriggerParam;

import java.util.List;

/**
 * @author 吴庆龙
 * @date 2020/6/17 10:11 上午
 */
public class ExecutorRouteFailOver extends ExecutorRouter {
    @Override
    public ResultT<String> route(TriggerParam triggerParam, List<String> addressList) {
        StringBuilder sb = new StringBuilder();
        for (String address : addressList) {
            // 心跳监测
            ResultT<String> beatResult;
            try {
                ExecutorBiz executorBiz = SnailJobScheduler.getExecutorBiz(address);
                beatResult = executorBiz.beat();
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                beatResult = new ResultT<>(ResultT.FAIL_CODE, e.getMessage());
            }

            sb.append("Address:").append(address);
            sb.append("Code:").append(beatResult.getCode());
            sb.append("Msg:").append(beatResult.getMsg());
            sb.append(";");

            if (beatResult.getCode() == ResultT.SUCCESS_CODE) {
                beatResult.setMsg(sb.toString());
                beatResult.setContent(address);
                return beatResult;
            }
        }
        return new ResultT<>(ResultT.FAIL_CODE, sb.toString());
    }
}
