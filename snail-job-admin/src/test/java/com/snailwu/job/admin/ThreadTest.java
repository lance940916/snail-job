package com.snailwu.job.admin;

import java.util.concurrent.TimeUnit;

/**
 * @author 吴庆龙
 * @date 2020/7/20 9:50 上午
 */
public class ThreadTest {
    public static void main(String[] args) throws InterruptedException {

        Thread thread = new Thread(() -> {
            System.out.println(Thread.currentThread().getState());
            System.out.println("hello Thread.");
        });
        thread.setDaemon(true);
        System.out.println(thread.getState());
        thread.start();
        System.out.println(thread.getState());

        TimeUnit.SECONDS.sleep(3);
        System.out.println(thread.getState());
    }
}
