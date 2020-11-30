package com.snailwu.job.core.node.impl;

import com.snailwu.job.core.biz.model.ResultT;
import com.snailwu.job.core.exception.JobException;
import com.snailwu.job.core.handler.annotation.SnailJob;
import com.snailwu.job.core.handler.impl.MethodJobHandler;
import com.snailwu.job.core.node.SnailJobNode;
import com.snailwu.job.core.node.SnailJobNodeProperties;
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
public class SnailJobSpringNode extends SnailJobNode
        implements ApplicationContextAware, SmartInitializingSingleton, DisposableBean {

    /**
     * Spring 上下文
     */
    private static ApplicationContext applicationContext;

    public SnailJobSpringNode(SnailJobNodeProperties configuration) {
        super(configuration);
    }

    @Override
    public void afterSingletonsInstantiated() {
        // 扫描所有的 JobHandler
        initJobHandlerMethodRepository();

        super.start();
    }

    /**
     * 扫描项目中的 JobHandler 并进行注册
     */
    private void initJobHandlerMethodRepository() {
        if (applicationContext == null) {
            LOGGER.error("[SnailJob]-Spring applicationContext为NULL");
            return;
        }

        // 获取 Spring 中的对象
        String[] beanNames = applicationContext.getBeanNamesForType(Object.class, false, true);
        for (String beanName : beanNames) {
            // 扫描 bean 中含有 SnailJob 注解的方法
            Object bean = applicationContext.getBean(beanName);
            Map<Method, SnailJob> methodJobMap = MethodIntrospector.selectMethods(
                    bean.getClass(),
                    new MethodIntrospector.MetadataLookup<SnailJob>() {
                        @Override
                        public SnailJob inspect(Method method) {
                            return AnnotatedElementUtils.findMergedAnnotation(method, SnailJob.class);
                        }
                    }
            );
            if (methodJobMap.isEmpty()) {
                continue;
            }

            for (Map.Entry<Method, SnailJob> entry : methodJobMap.entrySet()) {
                Method method = entry.getKey();
                SnailJob job = entry.getValue();

                String jobHandlerName = job.name();
                if (jobHandlerName.trim().length() == 0) {
                    throw new JobException("[SnailJob]-JobHandler的Name无效, " +
                            "for[" + bean.getClass() + "#" + method.getName() + "]");
                }
                if (loadJobHandler(jobHandlerName) != null) {
                    throw new RuntimeException("[SnailJob]-JobHandler的Name存在冲突, " +
                            "for[" + jobHandlerName + "]");
                }

                // 检验方法的参数. 1:只有一个参数; 2:参数必须是String类型的; 3:返回值必须是 ResultT 类型
                if (!(method.getParameterTypes().length == 1 && method.getParameterTypes()[0].isAssignableFrom(String.class))) {
                    throw new RuntimeException("[SnailJob]-JobHandler的方法入参无效, " +
                            "for[" + bean.getClass() + "#" + method.getName() + "], " +
                            "格式如:\" public ReturnT<String> execute(String param) \".");
                }
                if (!method.getReturnType().isAssignableFrom(ResultT.class)) {
                    throw new RuntimeException("[SnailJob]-JobHandler的方法返回值无效, " +
                            "for[" + bean.getClass() + "#" + method.getName() + "], " +
                            "格式如:\" public ReturnT<String> execute(String param) \".");
                }
                method.setAccessible(true);

                // 初始化 & 销毁方法
                Method initMethod = null;
                Method destroyMethod = null;
                if (job.init().trim().length() > 0) {
                    try {
                        initMethod = bean.getClass().getDeclaredMethod(job.init());
                        initMethod.setAccessible(true);
                    } catch (NoSuchMethodException e) {
                        throw new RuntimeException("[SnailJob]-初始化方法无效, " +
                                "for[" + bean.getClass() + "#" + method.getName() + "].");
                    }
                }
                if (job.destroy().trim().length() > 0) {
                    try {
                        destroyMethod = bean.getClass().getDeclaredMethod(job.destroy());
                        destroyMethod.setAccessible(true);
                    } catch (NoSuchMethodException e) {
                        throw new RuntimeException("[SnailJob]-销毁方法无效, " +
                                "for[" + bean.getClass() + "#" + method.getName() + "].");
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
        SnailJobSpringNode.applicationContext = applicationContext;
    }

    @Override
    public void destroy() {
        super.stop();
    }
}
