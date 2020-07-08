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

    /**
     * 回调队列
     */
    private final LinkedBlockingQueue<CallbackParam> callbackQueue = new LinkedBlockingQueue<>();

    /**
     * 添加回调任务到队列中
     */
    public static void pushCallback(CallbackParam callbackParam) {
        getInstance().callbackQueue.add(callbackParam);
        log.info("[SnailJob]-回调线程-添加任务到队列.logId:{}", callbackParam.getLogId());
    }

    /**
     * 回调线程
     */
    private Thread triggerCallbackThread;

    /**
     * 回调线程终止标志
     */
    private volatile boolean stopFlag = false;

    /**
     * 启动回调线程
     */
    public void start() {
        if (SnailJobExecutor.getAdminBiz() == null) {
            log.warn("[SnailJob]-回调线程-启动失败. 没有配置调度中心地址");
            return;
        }

        triggerCallbackThread = new Thread(() -> {
            while (!stopFlag) {
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
                    if (!stopFlag) {
                        // 没有主动进行中断，发生异常
                        log.error(e.getMessage(), e);
                    }
                }
            }

            // 线程被中断后，将剩余的回调任务执行完毕，再退出线程
            List<CallbackParam> callbackParamList = new ArrayList<>();
            getInstance().callbackQueue.drainTo(callbackParamList);
            if (!callbackParamList.isEmpty()) {
                doCallback(callbackParamList);
            }
        });

        triggerCallbackThread.setDaemon(true);
        triggerCallbackThread.setName("TriggerCallbackThread");
        triggerCallbackThread.start();

        // TODO 重试线程
    }

    /**
     * 进行回调
     */
    private void doCallback(List<CallbackParam> callbackParamList) {
        AdminBiz adminBiz = SnailJobExecutor.getAdminBiz();
        try {
            ResultT<String> resultT = adminBiz.callback(callbackParamList);
            if (resultT != null && ResultT.SUCCESS_CODE == resultT.getCode()) {
                log.info("[SnailJob]-回调线程-回调成功");
            } else {
                log.error("[SnailJob]-回调线程-回调失败");
            }
        } catch (Exception e) {
            log.error("[SnailJob]-回调线程-回调异常", e);
        }
        // TODO 失败重试
    }

    /**
     * 停止
     */
    public void stop() {
        stopFlag = true;

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
