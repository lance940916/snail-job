package com.snailwu.job.admin.constant;

/**
 * @author 吴庆龙
 * @date 2020/8/1 2:28 下午
 */
public interface JobConstants {

    /**
     * 请求ID,在拦截器中放在Header中,作为请求的唯一标识
     */
    String JOB_REQUEST_ID = "JOB_REQUEST_ID";

    /**
     * 请求ID,打印在日志中,作为请求的唯一标识
     */
    String JOB_LOG_ID = "JOB_LOG_ID";

    /**
     * 日期时间格式化格式
     */
    String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss,SSS";

    /**
     * 每间隔多少秒进行一次预读取任务
     */
    long PRE_LOAD_SLEEP_MS = 10000;

}
