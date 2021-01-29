package com.snailwu.job.admin.constant;

import com.snailwu.job.admin.core.cron.CronExpression;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 吴庆龙
 * @date 2020/8/1 2:28 下午
 */
public interface AdminConstants {

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
     * 每次获取任务的最大数量
     * 同时（一秒内）最大能调度 200 个任务
     */
    int MAX_LIMIT_PRE_READ = 200;

    /**
     * Cron 表达式对象缓存
     */
    Map<String, CronExpression> CRON_CACHE = new HashMap<>();

}
