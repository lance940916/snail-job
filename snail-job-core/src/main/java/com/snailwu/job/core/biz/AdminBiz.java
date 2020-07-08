package com.snailwu.job.core.biz;

import com.snailwu.job.core.biz.model.CallbackParam;
import com.snailwu.job.core.biz.model.RegistryParam;
import com.snailwu.job.core.biz.model.ResultT;

import java.util.List;

/**
 * @author 吴庆龙
 * @date 2020/5/25 2:38 下午
 */
public interface AdminBiz {

    /**
     * 回调任务执行结果
     */
    ResultT<String> callback(List<CallbackParam> callbackParamList);

    /**
     * 进行客户端的注册
     */
    ResultT<String> registry(RegistryParam registryParam);

    /**
     * 移除客户端
     */
    ResultT<String> registryRemove(RegistryParam registryParam);

}
