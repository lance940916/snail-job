package com.snailwu.job.admin;

import com.snailwu.job.admin.constant.HttpConstants;
import org.apache.logging.log4j.ThreadContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * @author 吴庆龙
 * @date 2020/7/20 9:50 上午
 */
public class ThreadLogTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(ThreadLogTest.class);

    public static void main(String[] args) throws InterruptedException {
        ThreadContext.put(HttpConstants.JOB_LOG_ID, "主线程");

        new Thread(() -> {
            ThreadContext.put(HttpConstants.JOB_LOG_ID, "线程1");
            while (true) {
                try {
                    TimeUnit.SECONDS.sleep(1);
                    LOGGER.info("Hello 线程1");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        new Thread(() -> {
            ThreadContext.put(HttpConstants.JOB_LOG_ID, "线程2");
            while (true) {
                try {
                    TimeUnit.SECONDS.sleep(1);
                    LOGGER.info("Hello 线程2");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        while (true) {
            try {
                TimeUnit.SECONDS.sleep(1);
                LOGGER.info("Hello 主线程");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
