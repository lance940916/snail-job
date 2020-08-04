package com.snailwu.job.core.thread;

import com.snailwu.job.core.biz.AdminBiz;
import com.snailwu.job.core.biz.model.CallbackParam;
import com.snailwu.job.core.biz.model.ResultT;
import com.snailwu.job.core.executor.SnailJobExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * 发送任务执行结果给调度中心
 *
 * @author 吴庆龙
 * @date 2020/5/26 4:43 下午
 */
public class ResultCallbackThread {
    private static final Logger LOGGER = LoggerFactory.getLogger(ResultCallbackThread.class);

    /**
     * 回调队列
     */
    private static final LinkedBlockingQueue<CallbackParam> CALLBACK_QUEUE = new LinkedBlockingQueue<>();

    /**
     * 添加回调任务到队列中
     */
    public static void addCallbackQueue(CallbackParam callbackParam) {
        CALLBACK_QUEUE.add(callbackParam);
        LOGGER.info("[SnailJob]-回调线程-添加到队列.logId:{}", callbackParam.getLogId());
    }

    /**
     * 回调线程
     */
    private static Thread callbackThread;

    /**
     * 回调线程终止标志
     */
    private static volatile boolean stopFlag = false;

    /**
     * 启动回调线程
     */
    public static void start() {
        if (SnailJobExecutor.getAdminBiz() == null) {
            LOGGER.warn("[SnailJob]-回调线程-启动失败.没有配置调度中心地址");
            return;
        }

        callbackThread = new Thread(() -> {
            while (!stopFlag) {
                try {
                    // 从队列中获取回调参数（回调任务）
                    CallbackParam callbackParam = CALLBACK_QUEUE.take();

                    // 有任务进行回调
                    doCallback(callbackParam);
                } catch (InterruptedException e) {
                    if (!stopFlag) {
                        LOGGER.error("任务结果回调线程被终端.", e);
                    }
                } catch (Exception e) {
                    LOGGER.error("回调任务发生异常.", e);
                }
            }

            // 线程被中断后，将剩余的回调任务执行完毕，再退出线程
            if (!CALLBACK_QUEUE.isEmpty()) {
                CALLBACK_QUEUE.forEach(ResultCallbackThread::doCallback);
            }
        });

        callbackThread.setDaemon(true);
        callbackThread.setName("result-callback-thread");
        callbackThread.start();
        LOGGER.info("任务执行结果回调线程启动成功");
    }

    /**
     * 进行回调
     */
    private static void doCallback(CallbackParam callbackParam) {
        AdminBiz adminBiz = SnailJobExecutor.getAdminBiz();
        try {
            ResultT<String> result = adminBiz.callback(callbackParam);
            if (ResultT.SUCCESS_CODE == result.getCode()) {
                LOGGER.info("[SnailJob]-回调线程-回调成功");
            } else {
                LOGGER.error("[SnailJob]-回调线程-回调失败.原因:{}", result.getMsg());
            }
        } catch (Exception e) {
            LOGGER.error("[SnailJob]-回调线程-回调异常.", e);
        }
    }

    /**
     * 停止
     */
    public static void stop() {
        stopFlag = true;
        callbackThread.interrupt();
        try {
            callbackThread.join();
        } catch (InterruptedException e) {
            LOGGER.error(e.getMessage(), e);
        }
        LOGGER.info("任务执行结果回调线程停止成功");
    }

}
