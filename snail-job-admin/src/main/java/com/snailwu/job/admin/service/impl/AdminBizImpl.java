package com.snailwu.job.admin.service.impl;

import com.snailwu.job.admin.dao.SnailJobRegistryMapper;
import com.snailwu.job.core.biz.AdminBiz;
import com.snailwu.job.core.biz.model.CallbackParam;
import com.snailwu.job.core.biz.model.RegistryParam;
import com.snailwu.job.core.biz.model.ResultT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author 吴庆龙
 * @date 2020/6/3 11:34 上午
 */
@Service
public class AdminBizImpl implements AdminBiz {
    private static final Logger log = LoggerFactory.getLogger(AdminBizImpl.class);

    @Resource
    private SnailJobRegistryMapper registryMapper;

    @Override
    public ResultT<String> callback(List<CallbackParam> callbackParamList) {
        for (CallbackParam callbackParam : callbackParamList) {
            // TODO 将执行结果与任务执行日志关联起来
            log.info("回调成功. 任务执行结果: {}", callbackParam.getExecuteResult().getCode());
        }
        return ResultT.SUCCESS;
    }

    @Override
    public ResultT<String> registry(RegistryParam registryParam) {

        return ResultT.FAIL;
    }

    @Override
    public ResultT<String> registryRemove(RegistryParam registryParam) {

        return ResultT.SUCCESS;
    }
}
