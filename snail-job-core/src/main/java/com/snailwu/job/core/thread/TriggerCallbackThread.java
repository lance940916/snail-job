package com.snailwu.job.core.thread;

import com.snailwu.job.core.biz.AdminBiz;
import com.snailwu.job.core.biz.model.CallbackParam;
import com.snailwu.job.core.biz.model.ResultT;
import com.snailwu.job.core.executor.SnailJobExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 发送任务执行结果给调度中心
 *
 * @author 吴庆龙
 * @date 2020/5/26 4:43 下午
 */
public class TriggerCallbackThread {
    private static final Logger log = LoggerFactory.getLogger(TriggerCallbackThread.class);

    private static final TriggerCallbackThread instance = new TriggerCallbackThread();

    public static TriggerCallbackThread getInstance() {
        return instance;
    }

    private final LinkedBlockingQueue<CallbackParam> callbackQueue = new LinkedBlockingQueue<>();

    /**
     * 添加回调参数（回调任务）到队列中
     */
    public static void pushCallback(CallbackParam callbackParam) {
        getInstance().callbackQueue.add(callbackParam);
        log.info("snail-job, push callback request, logId:{}", callbackParam.getLogId());
    }

    /**
     * 回调线程
     */
    private Thread triggerCallbackThread;

    /**
     * 回调重试线程
     */
    private Thread triggerRetryCallbackThread;

    /**
     * 回调线程是否停止
     */
    private volatile boolean toStop = false;

    /**
     * 启动回调线程
     */
    public void start() {
        if (SnailJobExecutor.getAdminBiz() == null) {
            log.warn("snail-job, executor callback config fail, adminAddress is null.");
            return;
        }

        triggerCallbackThread = new Thread(() -> {
            while (!toStop) {
                try {
                    // 从队列中获取回调参数（回调任务）
                    CallbackParam callbackParam = getInstance().callbackQueue.take();

                    // 获取队列中所有的回调任务
                    List<CallbackParam> callbackParamList = new ArrayList<>();
                    getInstance().callbackQueue.drainTo(callbackParamList);
                    callbackParamList.add(callbackParam);

                    // 有任务进行回调
                    if (!callbackParamList.isEmpty()) {
                        doCallback(callbackParamList);
                    }
                } catch (InterruptedException e) {
                    if (!toStop) {
                        // 没有主动进行中断，发生异常
                        log.error(e.getMessage(), e);
                    }
                }
            }

            // last callback
            List<CallbackParam> callbackParamList = new ArrayList<>();
            getInstance().callbackQueue.drainTo(callbackParamList);
            if (!callbackParamList.isEmpty()) {
                doCallback(callbackParamList);
            }
        });

        triggerCallbackThread.setDaemon(true);
        triggerCallbackThread.setName("triggerCallbackThread");
        triggerCallbackThread.start();

        // TODO 重试线程
    }

    /**
     * 调用
     */
    private void doCallback(List<CallbackParam> callbackParamList) {
        boolean callbackRet = false;

        // 获取 AdminBizClient
        AdminBiz adminBiz = SnailJobExecutor.getAdminBiz();

        // 尝试进行回调
        try {
            ResultT<String> resultT = adminBiz.callback(callbackParamList);
            if (resultT != null && ResultT.SUCCESS_CODE == resultT.getCode()) {
                log.info("snail-job job callback finish.");
                callbackRet = true;
            } else {
                log.error("snail-job job callback fail.");
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
//        if (!callbackRet) {
//             TODO 失败重试
//        }
    }

    /**
     * 停止
     */
    public void stop() {
        toStop = true;

        if (triggerCallbackThread != null) {
            triggerCallbackThread.interrupt();
            try {
                triggerCallbackThread.join();
            } catch (InterruptedException e) {
                log.error(e.getMessage(), e);
            }
        }
    }

}
