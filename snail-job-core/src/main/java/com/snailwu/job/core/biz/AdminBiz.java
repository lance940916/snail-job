package com.snailwu.job.core.biz;

import com.snailwu.job.core.biz.model.CallbackParam;
import com.snailwu.job.core.biz.model.RegistryParam;
import com.snailwu.job.core.biz.model.ResultT;

/**
 * @author 吴庆龙
 * @date 2020/5/25 2:38 下午
 */
public interface AdminBiz {

    /**
     * 回调任务执行结果
     * @param callbackParam 回调参数
     * @return 回调结果
     */
    ResultT<String> callback(CallbackParam callbackParam);

    /**
     * 进行客户端的注册
     * @param registryParam 注册参数
     * @return 注册结果
     */
    ResultT<String> registryNode(RegistryParam registryParam);

    /**
     * 移除客户端
     * @param registryParam 移除参数
     * @return 移除结果
     */
    ResultT<String> removeNode(RegistryParam registryParam);

}
