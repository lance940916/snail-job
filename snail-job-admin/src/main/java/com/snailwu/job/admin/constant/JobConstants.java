package com.snailwu.job.admin.constant;

/**
 * @author 吴庆龙
 * @date 2020/8/1 2:28 下午
 */
public interface JobConstants {

    /**
     * 请求ID,在拦截器中放在Header中,作为请求的唯一标识
     */
    String REQUEST_ID = "JOB_REQUEST_ID";

    /**
     * 请求ID,打印在日志中,作为请求的唯一标识
     */
    String LOG_ID = "JOB_LOG_ID";

    /**
     * 日期时间格式化格式
     */
    String DATE_TIME_MS_PATTERN = "yyyy-MM-dd HH:mm:ss,SSS";
    String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

    /**
     * 任务执行间隔时间
     */
    long SCAN_JOB_SLEEP_MS = 10000;

    /**
     * 提前准备任务调度的时间跨度
     */
    long SCAN_JOB_PRE_READ_MS = 20000;

    /**
     * 获取不到锁时休眠时长
     */
    long SCAN_JOB_LOCK_FAIL_SLEEP_MS = 5000;

    /**
     * 每次获取任务的最大数量
     * 同时（一秒内）能调度 200 个任务
     */
    int MAX_LIMIT_PRE_READ = 200;

}
