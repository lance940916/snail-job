package com.snailwu.job.core.thread;

import com.snailwu.job.core.biz.AdminBiz;
import com.snailwu.job.core.biz.model.CallbackParam;
import com.snailwu.job.core.biz.model.ResultT;
import com.snailwu.job.core.executor.JobExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * 发送任务执行结果给调度中心
 *
 * @author 吴庆龙
 * @date 2020/5/26 4:43 下午
 */
public class CallbackThread {
    private static final Logger LOGGER = LoggerFactory.getLogger(CallbackThread.class);

    /**
     * 回调队列
     */
    private static final LinkedBlockingQueue<CallbackParam> CALLBACK_QUEUE = new LinkedBlockingQueue<>();

    /**
     * 回调线程
     */
    private static Thread thread;

    /**
     * 回调线程终止标志
     */
    private static volatile boolean running = true;

    /**
     * 添加回调任务到队列中
     */
    public static void addCallbackQueue(CallbackParam callbackParam) {
        CALLBACK_QUEUE.add(callbackParam);
    }

    /**
     * 启动回调线程
     */
    public static void start() {
        // 没有配置调度中心，不需要启动
        if (JobExecutor.getAdminBiz() == null) {
            return;
        }

        thread = new Thread(() -> {
            while (running) {
                try {
                    // 获取回调任务
                    CallbackParam callbackParam = CALLBACK_QUEUE.take();

                    // 进行回调
                    doCallback(callbackParam);
                } catch (InterruptedException e) {
                    String errorMsg = running ? "回调线程被正常中断":"回调线程被异常中断";
                    LOGGER.error(errorMsg);
                } catch (Exception e) {
                    // 本次回调失败 TODO 回调失败如何处理？？？
                    LOGGER.error("回调任务发生异常。", e);
                }
            }

            // 线程被中断后，将剩余的回调任务执行完毕，再退出线程
            if (!CALLBACK_QUEUE.isEmpty()) {
                CALLBACK_QUEUE.forEach(callbackParam -> {
                    try {
                        // 进行回调
                        doCallback(callbackParam);
                    } catch (Exception e) {
                        // 本次回调失败 TODO 回调失败如何处理？？？
                        LOGGER.error("回调任务发生异常。", e);
                    }
                });
            }
        });

        thread.setDaemon(true);
        thread.setName("callback-thread");
        thread.start();
        LOGGER.info("回调线程-已启动。");
    }

    /**
     * 进行回调
     */
    private static void doCallback(CallbackParam callbackParam) {
        AdminBiz adminBiz = JobExecutor.getAdminBiz();
        ResultT<String> result = adminBiz.callback(callbackParam);
        if (ResultT.SUCCESS_CODE != result.getCode()) {
            LOGGER.error("回调返回失败。原因：{}", result.toString());
        }
    }

    /**
     * 停止
     */
    public static void stop() {
        running = false;
        try {
            thread.interrupt();
            thread.join();
            LOGGER.info("回调线程-已停止。");
        } catch (InterruptedException e) {
            LOGGER.error("停止线程 {} 异常", thread.getName(), e);
        }
    }

}
