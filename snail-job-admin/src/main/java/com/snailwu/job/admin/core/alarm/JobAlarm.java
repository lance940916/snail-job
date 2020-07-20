package com.snailwu.job.admin.core.alarm;

import com.snailwu.job.admin.core.model.JobInfo;
import com.snailwu.job.admin.core.model.JobLog;

/**
 * @author 吴庆龙
 * @date 2020/7/20 5:25 下午
 */
public interface JobAlarm {

    boolean doAlarm(JobInfo jobInfo, JobLog jobLog);

}
