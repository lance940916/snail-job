package com.snailwu.job.core.handler.impl;

import com.snailwu.job.core.biz.model.ResultT;
import com.snailwu.job.core.handler.IJobHandler;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author 吴庆龙
 * @date 2020/5/26 11:38 上午
 */
public class MethodJobHandler extends IJobHandler {

    private final Object target;
    private final Method method;
    private final Method initMethod;
    private final Method destroyMethod;

    public MethodJobHandler(Object target, Method method, Method initMethod, Method destroyMethod) {
        this.target = target;
        this.method = method;

        this.initMethod = initMethod;
        this.destroyMethod = destroyMethod;
    }

    @Override
    public void init() throws InvocationTargetException, IllegalAccessException {
        if (initMethod != null) {
            initMethod.invoke(target);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public ResultT<String> execute(String param) throws Exception {
        return (ResultT<String>) method.invoke(target, param);
    }

    @Override
    public void destroy() throws InvocationTargetException, IllegalAccessException {
        if (destroyMethod != null) {
            destroyMethod.invoke(target);
        }
    }
}
