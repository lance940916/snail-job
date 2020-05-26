package com.snailwu.job.core.handler;

import com.snailwu.job.core.biz.model.ResultT;

import java.lang.reflect.InvocationTargetException;

/**
 * @author 吴庆龙
 * @date 2020/5/26 11:35 上午
 */
public abstract class IJobHandler {

    public void init() throws InvocationTargetException, IllegalAccessException {
    }

    public abstract ResultT<String> execute(String param) throws Exception;

    public void destroy() throws InvocationTargetException, IllegalAccessException {
    }

}
