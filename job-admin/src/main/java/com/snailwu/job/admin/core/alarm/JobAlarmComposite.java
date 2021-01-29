package com.snailwu.job.admin.core.alarm;

import com.snailwu.job.admin.model.JobInfo;
import com.snailwu.job.admin.model.JobLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 报警集
 *
 * @author 吴庆龙
 * @date 2020/7/20 5:25 下午
 */
@Component
public class JobAlarmComposite implements ApplicationContextAware, InitializingBean {
    private static final Logger LOGGER = LoggerFactory.getLogger(JobAlarmComposite.class);

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    /**
     * 报警方式集合
     */
    private List<JobAlarm> alarmList;

    @Override
    public void afterPropertiesSet() {
        Map<String, JobAlarm> jobAlarmMap = applicationContext.getBeansOfType(JobAlarm.class);
        if (!CollectionUtils.isEmpty(jobAlarmMap)) {
            alarmList = new ArrayList<>(jobAlarmMap.values());
        }
    }

    /**
     * 进行报警，所有报警都成功才是成功
     *
     * @return true=报警成功;false=报警失败
     */
    public boolean alarm(JobInfo jobInfo, JobLog jobLog) {
        if (CollectionUtils.isEmpty(alarmList)) {
            return false;
        }

        boolean result = true;
        for (JobAlarm jobAlarm : alarmList) {
            boolean resultItem = false;
            try {
                resultItem = jobAlarm.doAlarm(jobInfo, jobLog);
            } catch (Exception e) {
                LOGGER.error("告警异常。", e);
            }
            if (!resultItem) {
                result = false;
            }
        }
        return result;
    }

}
