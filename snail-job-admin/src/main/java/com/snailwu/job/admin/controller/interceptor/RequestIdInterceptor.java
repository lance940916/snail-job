package com.snailwu.job.admin.controller.interceptor;

import org.apache.logging.log4j.ThreadContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.NamedThreadLocal;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.snailwu.job.admin.constant.HttpConstants.JOB_LOG_ID;
import static com.snailwu.job.admin.constant.HttpConstants.JOB_REQUEST_ID;

/**
 * 将所有的请求加入请求ID
 * - 使用时间戳作为请求的ID
 *
 * @author 吴庆龙
 * @date 2020/7/28 7:15
 */
@Component
public class RequestIdInterceptor implements HandlerInterceptor {
    private static final Logger LOGGER = LoggerFactory.getLogger(RequestIdInterceptor.class);

    /**
     * 记录请求的耗时
     */
    private final NamedThreadLocal<Long> REQUEST_STOP_WATCH = new NamedThreadLocal<>("JobRequestStopWatch");

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 开始计时
        REQUEST_STOP_WATCH.set(System.currentTimeMillis());

        // 生成ID
        String requestId = createJobRequestId();

        // set log
        ThreadContext.put(JOB_LOG_ID, requestId);

        // set request
        String jobRequestId = request.getHeader(JOB_REQUEST_ID);
        if (jobRequestId == null || jobRequestId.trim().length() == 0) {
            request.setAttribute(JOB_REQUEST_ID, requestId);
        }
        LOGGER.info("[SnailJob]-请求:[{} {}],匹配方法:[{}]",
                request.getMethod(), request.getRequestURI(), handler.getClass());
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        Long beginTime = REQUEST_STOP_WATCH.get();
        long endTime = System.currentTimeMillis();
        LOGGER.info("[SnailJob]-响应:[{} {}],耗时:{}毫秒",
                request.getMethod(), request.getRequestURI(), (endTime - beginTime));
        ThreadContext.clearAll();
    }

    /**
     * 创建请求ID
     */
    private String createJobRequestId() {
        return System.currentTimeMillis() + "";
    }
}
