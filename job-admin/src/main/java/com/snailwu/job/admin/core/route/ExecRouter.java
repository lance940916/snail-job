package com.snailwu.job.admin.core.route;

import com.snailwu.job.core.biz.model.ResultT;
import com.snailwu.job.core.biz.model.TriggerParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 任务路由策略实现
 *
 * @author 吴庆龙
 * @date 2020/6/4 11:23 上午
 */
public abstract class ExecRouter {
    protected static final Logger LOGGER = LoggerFactory.getLogger(ExecRouter.class);

    public static final String NO_FOUND_ADDRESS_MSG = "无可用的执行器";

    /**
     * route address
     * @param triggerParam 触发参数
     * @param addresses 地址列表
     * @return 地址
     */
    public abstract ResultT<String> route(TriggerParam triggerParam, String[] addresses);

}
