package com.snailwu.job.core.executor.impl;

import com.snailwu.job.core.biz.model.ResultT;
import com.snailwu.job.core.executor.JobExecutor;
import com.snailwu.job.core.executor.model.ExecutorConfiguration;
import com.snailwu.job.core.handler.annotation.SnailJob;
import com.snailwu.job.core.handler.impl.MethodJobHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.MethodIntrospector;
import org.springframework.core.annotation.AnnotatedElementUtils;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * Spring 的执行器节点配置
 *
 * @author 吴庆龙
 * @date 2020/5/26 10:45 上午
 */
public class JobSpringExecutor extends JobExecutor
        implements ApplicationContextAware, SmartInitializingSingleton, DisposableBean {
    public static final Logger log = LoggerFactory.getLogger(JobSpringExecutor.class);

    /**
     * Spring 上下文
     */
    private static ApplicationContext applicationContext;

    public JobSpringExecutor(ExecutorConfiguration executorConfiguration) {
        super(executorConfiguration);
    }

    @Override
    public void afterSingletonsInstantiated() {
        // 初始化所有的 JobHandler
        initJobHandlerMethodRepository();

        super.start();
    }

    /**
     * 获取所有的 JobHandler 并注册到 Map 中
     */
    private void initJobHandlerMethodRepository() {
        if (applicationContext == null) {
            return;
        }

        String[] beanNames = applicationContext.getBeanNamesForType(Object.class, false, true);
        for (String beanName : beanNames) {
            Object bean = applicationContext.getBean(beanName);
            Map<Method, SnailJob> methodSnailJobMap = MethodIntrospector.selectMethods(bean.getClass(),
                    new MethodIntrospector.MetadataLookup<SnailJob>() {
                        @Override
                        public SnailJob inspect(Method method) {
                            return AnnotatedElementUtils.findMergedAnnotation(method, SnailJob.class);
                        }
                    });
            if (methodSnailJobMap.isEmpty()) {
                continue;
            }

            for (Map.Entry<Method, SnailJob> methodSnailJobEntry : methodSnailJobMap.entrySet()) {
                Method method = methodSnailJobEntry.getKey();
                SnailJob snailJob = methodSnailJobEntry.getValue();

                String jobHandlerName = snailJob.value();
                if (jobHandlerName.trim().length() == 0) {
                    throw new RuntimeException("method-jobHandler name invalid, " +
                            "for[" + bean.getClass() + "#" + method.getName() + "] .");
                }
                if (loadJobHandler(jobHandlerName) != null) {
                    throw new RuntimeException("snail-job jobHandler[" + jobHandlerName + "] name conflicts");
                }

                // execute method
                if (!(method.getParameterTypes().length == 1 && method.getParameterTypes()[0].isAssignableFrom(String.class))) {
                    throw new RuntimeException("snail-job method-jobHandler param-classType invalid, " +
                            "for[" + bean.getClass() + "#" + method.getName() + "] , " +
                            "The correct method format like \" public ReturnT<String> execute(String param) \" .");
                }
                if (!method.getReturnType().isAssignableFrom(ResultT.class)) {
                    throw new RuntimeException("snail-job method-jobHandler return-classType invalid, " +
                            "for[" + bean.getClass() + "#" + method.getName() + "] , " +
                            "The correct method format like \" public ReturnT<String> execute(String param) \" .");
                }
                method.setAccessible(true);

                Method initMethod = null;
                Method destroyMethod = null;
                if (snailJob.init().trim().length() > 0) {
                    try {
                        initMethod = bean.getClass().getDeclaredMethod(snailJob.init());
                        initMethod.setAccessible(true);
                    } catch (NoSuchMethodException e) {
                        throw new RuntimeException("snail-job method-jobHandler initMethod invalid, " +
                                "for[" + bean.getClass() + "#" + method.getName() + "] .");
                    }
                }
                if (snailJob.destroy().trim().length() > 0) {
                    try {
                        destroyMethod = bean.getClass().getDeclaredMethod(snailJob.destroy());
                        destroyMethod.setAccessible(true);
                    } catch (NoSuchMethodException e) {
                        throw new RuntimeException("snail-job method-jobHandler initMethod invalid, " +
                                "for[" + bean.getClass() + "#" + method.getName() + "] .");
                    }
                }

                // 注册
                MethodJobHandler methodJobHandler = new MethodJobHandler(bean, method, initMethod, destroyMethod);
                registryJobHandler(jobHandlerName, methodJobHandler);
            }
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        JobSpringExecutor.applicationContext = applicationContext;
    }

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    @Override
    public void destroy() throws Exception {
        super.stop();
    }
}
