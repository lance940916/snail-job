package com.snailwu.job.admin;

/**
 * @author 吴庆龙
 * @date 2020/7/30 4:43 下午
 */
public class TimeTest {
    public static void main(String[] args) throws InterruptedException {

//        long preReadMs = 5000;
//        while (true) {
//            long startTs = System.currentTimeMillis();
//            System.out.println("当前时间:" + DateFormatUtils.format(startTs, DATE_TIME_PATTERN));
//
//            long curTs = startTs / 1000 * 1000;
//
//            System.out.println("读取 [" + DateFormatUtils.format(curTs, DATE_TIME_PATTERN)
//                    + "]-[" + DateFormatUtils.format(curTs + preReadMs, DATE_TIME_PATTERN) + "] 之间的数据");
//
//            TimeUnit.SECONDS.sleep(RandomUtils.nextInt(1, 4));
//
//            long costMs = System.currentTimeMillis() - curTs;
//            System.out.println("耗时:" + costMs + "毫秒");
//
//            TimeUnit.MILLISECONDS.sleep(preReadMs - costMs);
//        }

        long nowTimeTs = System.currentTimeMillis();
        System.out.println(nowTimeTs / 1000 % 60);

    }
}
