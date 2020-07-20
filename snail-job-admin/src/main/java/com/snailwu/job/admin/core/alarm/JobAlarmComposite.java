package com.snailwu.job.admin.core.alarm;

import com.snailwu.job.admin.core.model.JobInfo;
import com.snailwu.job.admin.core.model.JobLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author 吴庆龙
 * @date 2020/7/20 5:25 下午
 */
@Component
public class JobAlarmComposite implements ApplicationContextAware, InitializingBean {
    private static final Logger log = LoggerFactory.getLogger(JobAlarmComposite.class);

    private ApplicationContext applicationContext;
    private List<JobAlarm> alarmList;

    @Override
    public void afterPropertiesSet() throws Exception {
        Map<String, JobAlarm> jobAlarmMap = applicationContext.getBeansOfType(JobAlarm.class);
        if (!jobAlarmMap.isEmpty()) {
            alarmList = new ArrayList<>(jobAlarmMap.values());
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public boolean alarm(JobInfo jobInfo, JobLog jobLog) {
        boolean result = false;
        if (alarmList == null || alarmList.isEmpty()) {
            return result;
        }

        result = true;
        for (JobAlarm jobAlarm : alarmList) {
            boolean resultItem = false;
            try {
                resultItem = jobAlarm.doAlarm(jobInfo, jobLog);
            } catch (Exception e) {
                log.error("告警异常,异常消息:{}", e.getMessage());
            }
            if (!resultItem) {
                result = false;
            }
        }
        return result;
    }

}
