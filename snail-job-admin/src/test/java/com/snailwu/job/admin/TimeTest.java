package com.snailwu.job.admin;

import org.apache.commons.lang3.time.DateFormatUtils;

import java.util.concurrent.TimeUnit;

/**
 * @author 吴庆龙
 * @date 2020/7/30 4:43 下午
 */
public class TimeTest {
    public static void main(String[] args) throws InterruptedException {

//        long nowTimeTs = System.currentTimeMillis();

//        TimeUnit.MILLISECONDS.sleep(1000 - System.currentTimeMillis() % 1000);
//        System.out.println(System.currentTimeMillis());
//
//        TimeUnit.MILLISECONDS.sleep(1000 - System.currentTimeMillis() % 1000);
//        System.out.println(System.currentTimeMillis());
//
//        TimeUnit.MILLISECONDS.sleep(1000 - System.currentTimeMillis() % 1000);
//        System.out.println(System.currentTimeMillis());

        long ts = System.currentTimeMillis();
//        System.out.println(DateFormatUtils.format(ts, "yyyy-MM-dd HH:mm:ss,SSS"));
//        int invokeSecond = (int) ((ts / 1000) % 60);
//        System.out.println(invokeSecond);

        TimeUnit.MILLISECONDS.sleep(1000 - ts % 1000);
        ts = System.currentTimeMillis();
        long newTs = ts - 999;
        System.out.println(System.currentTimeMillis());
        System.out.println(DateFormatUtils.format(ts, "yyyy-MM-dd HH:mm:ss,SSS"));
        System.out.println(DateFormatUtils.format(newTs, "yyyy-MM-dd HH:mm:ss,SSS"));



    }
}
