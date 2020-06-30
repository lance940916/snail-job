package com.snailwu.job.admin.core.route.strategy;

import com.snailwu.job.admin.core.route.ExecutorRouter;
import com.snailwu.job.admin.core.scheduler.SnailJobScheduler;
import com.snailwu.job.core.biz.ExecutorBiz;
import com.snailwu.job.core.biz.model.IdleBeatParam;
import com.snailwu.job.core.biz.model.ResultT;
import com.snailwu.job.core.biz.model.TriggerParam;

import java.util.List;

/**
 * 忙碌转移
 *
 * @author 吴庆龙
 * @date 2020/6/17 10:11 上午
 */
public class ExecutorRouteBusyOver extends ExecutorRouter {
    @Override
    public ResultT<String> route(TriggerParam triggerParam, List<String> addressList) {
        StringBuilder sb = new StringBuilder();
        for (String address : addressList) {
            // 繁忙监测
            ResultT<String> idleBeatResult;
            try {
                ExecutorBiz executorBiz = SnailJobScheduler.getExecutorBiz(address);
                idleBeatResult = executorBiz.idleBeat(new IdleBeatParam(triggerParam.getJobId()));
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                idleBeatResult = new ResultT<>(ResultT.FAIL_CODE, e.getMessage());
            }

            sb.append("Address:").append(address);
            sb.append("Code:").append(idleBeatResult.getCode());
            sb.append("Msg:").append(idleBeatResult.getMsg());
            sb.append(";");

            // 不忙
            if (idleBeatResult.getCode() == ResultT.SUCCESS_CODE) {
                idleBeatResult.setMsg(sb.toString());
                idleBeatResult.setContent(address);
                return idleBeatResult;
            }
        }
        return new ResultT<>(ResultT.FAIL_CODE, sb.toString());
    }
}
