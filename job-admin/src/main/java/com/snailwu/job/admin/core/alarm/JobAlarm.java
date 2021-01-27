package com.snailwu.job.admin.core.alarm;

import com.snailwu.job.admin.model.JobInfo;
import com.snailwu.job.admin.model.JobLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author 吴庆龙
 * @date 2020/7/20 5:25 下午
 */
public interface JobAlarm {
    Logger LOGGER = LoggerFactory.getLogger(JobAlarm.class);

    /**
     * 报警方法
     * @param jobInfo 任务信息类
     * @param jobLog 执行日志类
     * @return true:报警成功；false:报警失败
     */
    boolean doAlarm(JobInfo jobInfo, JobLog jobLog);

}
