package com.snailwu.job.admin.constant;

import com.snailwu.job.admin.core.model.JobInfo;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author 吴庆龙
 * @date 2020/8/1 2:28 下午
 */
public interface JobConstants {

    /**
     * 日期时间格式化格式
     */
    String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss,SSS";

    /**
     * 任务库
     */
    ConcurrentHashMap<Long, JobInfo> JOB_INFO_REPOSITORY = new ConcurrentHashMap<>();



}
