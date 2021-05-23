package com.snailwu.job.core.handler;

import com.snailwu.job.core.biz.model.ResultT;

/**
 * 任务调度接口
 *
 * @author 吴庆龙
 * @date 2020/5/26 11:35 上午
 */
public abstract class IJobHandler {

    public void init() throws Exception {
    }

    /**
     * 具体的执行逻辑
     *
     * @param param 参数
     * @return 返回值
     * @throws Exception 异常
     */
    public abstract ResultT<String> execute(String param) throws Exception;

    public void destroy() throws Exception {
    }

}
