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

    ResultT<String> callback(List<CallbackParam> callbackParamList);

    ResultT<String> registry(RegistryParam registryParam);

    ResultT<String> registryRemove(RegistryParam registryParam);

}
