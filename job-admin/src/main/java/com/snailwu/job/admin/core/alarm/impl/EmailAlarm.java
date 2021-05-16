package com.snailwu.job.admin.core.alarm.impl;

import com.snailwu.job.admin.core.alarm.JobAlarm;
import com.snailwu.job.admin.model.JobInfo;
import com.snailwu.job.admin.model.JobLog;
import org.springframework.mail.javamail.JavaMailSender;

import javax.annotation.Resource;

/**
 * TODO 邮件报警
 *
 * @author 吴庆龙
 * @date 2020/7/20 5:43 下午
 */
//@Component
public class EmailAlarm implements JobAlarm {

    @Resource
    private JavaMailSender mailSender;

    @Override
    public boolean doAlarm(JobInfo info, JobLog log) {
        return true;
    }

}
