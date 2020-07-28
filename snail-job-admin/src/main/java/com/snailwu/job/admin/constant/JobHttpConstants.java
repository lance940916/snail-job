package com.snailwu.job.admin.constant;

/**
 * @author 吴庆龙
 * @date 2020/7/28 7:22
 */
public interface JobHttpConstants {

    /**
     * 请求ID,在拦截器中放在Header中,作为请求的唯一标识
     */
    String JOB_REQUEST_ID = "JOB_REQUEST_ID";

    /**
     * 请求ID,打印在日志中,作为请求的唯一标识
     */
    String JOB_LOG_ID = "JOB_LOG_ID";

}
