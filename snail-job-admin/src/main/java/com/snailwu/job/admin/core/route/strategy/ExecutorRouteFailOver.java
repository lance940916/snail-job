package com.snailwu.job.admin.core.route.strategy;

import com.snailwu.job.admin.core.route.ExecutorRouter;
import com.snailwu.job.admin.core.scheduler.JobScheduler;
import com.snailwu.job.core.biz.NodeBiz;
import com.snailwu.job.core.biz.model.ResultT;
import com.snailwu.job.core.biz.model.TriggerParam;

import java.util.List;

/**
 * 心跳监测在线可用执行器
 *
 * @author 吴庆龙
 * @date 2020/6/17 10:11 上午
 */
public class ExecutorRouteFailOver extends ExecutorRouter {
    @Override
    public ResultT<String> route(TriggerParam triggerParam, List<String> addressList) {
        for (String address : addressList) {
            ResultT<String> beatResult;
            try {
                NodeBiz nodeBiz = JobScheduler.getExecutorBiz(address);
                beatResult = nodeBiz.beat();
            } catch (Exception e) {
                LOGGER.error("选择执行器.idleBeat接口异常,执行器:{},原因:{}", address, e.getMessage());
                continue;
            }
            if (beatResult.getCode() == ResultT.SUCCESS_CODE) {
                return new ResultT<>(address);
            } else {
                LOGGER.info("选择执行器.执行器:[{}]忙碌,继续寻找执行器...", address);
            }
        }
        return new ResultT<>(ResultT.FAIL_CODE, NO_FOUND_ADDRESS_MSG);
    }
}
