package com.snailwu.job.core.executor.impl;

import com.snailwu.job.core.biz.model.ResultT;
import com.snailwu.job.core.exception.JobException;
import com.snailwu.job.core.executor.JobExecutor;
import com.snailwu.job.core.handler.annotation.ScheduleJob;
import com.snailwu.job.core.handler.impl.MethodJobHandler;
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
 * 依赖于 Spring 框架的扫描器
 * 扫描标有 @SnailJob 注解的方法注册到 JOB_HANDLER_REPOSITORY 中
 *
 * @author 吴庆龙
 * @date 2020/5/26 10:45 上午
 */
public class JobSpringExecutor extends JobExecutor
        implements ApplicationContextAware, SmartInitializingSingleton, DisposableBean {

    /**
     * Spring 上下文
     */
    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void afterSingletonsInstantiated() {
        // 扫描注册的 JobHandler
        initJobHandlerMethodRepository();

        super.start();
    }

    /**
     * 扫描项目中的 JobHandler 并进行注册
     */
    private void initJobHandlerMethodRepository() {
        if (applicationContext == null) {
            LOGGER.error("没有Spring应用的上下文，无法扫描JobHandler。");
            return;
        }

        // 获取 Spring 中的所有 bean
        String[] beanNames = applicationContext.getBeanNamesForType(Object.class, false,
                true);

        // 扫描 bean 中含有 Job 注解的方法
        for (String beanName : beanNames) {
            Object bean = applicationContext.getBean(beanName);
            Map<Method, ScheduleJob> methodJobMap = getMethodJobMap(bean);
            if (methodJobMap.isEmpty()) {
                continue;
            }

            // 注册类中的任务
            registryMethods(bean, methodJobMap);
        }
    }

    /**
     * 注册这个类中的任务
     *
     * @param bean         类
     * @param methodJobMap 任务集合
     */
    private void registryMethods(Object bean, Map<Method, ScheduleJob> methodJobMap) {
        for (Map.Entry<Method, ScheduleJob> entry : methodJobMap.entrySet()) {
            Method method = entry.getKey();
            ScheduleJob job = entry.getValue();

            // 校验任务名
            String jobName = job.name();
            if (jobName.trim().length() == 0) {
                throw new JobException("任务的Name无效, for[" + bean.getClass() + "#" + method.getName() + "]");
            }
            if (loadJobHandler(jobName) != null) {
                throw new JobException("任务的Name存在冲突, for[" + jobName + "]");
            }
            // 初始化方法
            Method initMethod = setMethodAccessible(bean, job.init());
            // 销毁方法
            Method destroyMethod = setMethodAccessible(bean, job.destroy());

            // 检验方法的参数. 1:只有一个参数; 2:参数必须是String类型的; 3:返回值必须是 ResultT 类型
            if (!(method.getParameterTypes().length == 1
                    && method.getParameterTypes()[0].isAssignableFrom(String.class))) {
                throw new JobException("JobHandler的方法入参无效, " +
                        "for[" + bean.getClass() + "#" + method.getName() + "], " +
                        "格式如:\" public ReturnT<String> execute(String param) \".");
            }
            if (!method.getReturnType().isAssignableFrom(ResultT.class)) {
                throw new JobException("JobHandler的方法返回值无效, " +
                        "for[" + bean.getClass() + "#" + method.getName() + "], " +
                        "格式如:\" public ReturnT<String> execute(String param) \".");
            }
            method.setAccessible(true);

            // 注册 JobHandler
            registryJobHandler(jobName, new MethodJobHandler(bean, method, initMethod, destroyMethod));
        }
    }

    /**
     * 设置方法可访问
     * method.setAccessible(true);
     */
    private Method setMethodAccessible(Object bean, String methodName) {
        Method method = null;
        if (methodName.trim().length() > 0) {
            try {
                method = bean.getClass().getDeclaredMethod(methodName);
                method.setAccessible(true);
            } catch (NoSuchMethodException e) {
                throw new JobException("销毁方法无效, for[" + bean.getClass() + "#" + methodName + "].");
            }
        }
        return method;
    }

    /**
     * 获取类中的方法注解集合
     *
     * @param bean 待扫描的类
     * @return key:方法实例，val:注解实例
     */
    private Map<Method, ScheduleJob> getMethodJobMap(Object bean) {
        return MethodIntrospector.selectMethods(
                bean.getClass(),
                new MethodIntrospector.MetadataLookup<ScheduleJob>() {
                    @Override
                    public ScheduleJob inspect(Method method) {
                        return AnnotatedElementUtils.findMergedAnnotation(method, ScheduleJob.class);
                    }
                }
        );
    }

    @Override
    public void destroy() {
        super.stop();
    }
}
