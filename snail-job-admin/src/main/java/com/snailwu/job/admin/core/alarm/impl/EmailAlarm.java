package com.snailwu.job.admin.core.alarm.impl;

import com.snailwu.job.admin.core.alarm.JobAlarm;
import com.snailwu.job.admin.core.conf.AdminConfig;
import com.snailwu.job.admin.core.model.JobGroup;
import com.snailwu.job.admin.core.model.JobInfo;
import com.snailwu.job.admin.core.model.JobLog;
import com.snailwu.job.admin.mapper.JobGroupDynamicSqlSupport;
import com.snailwu.job.core.biz.model.ResultT;
import org.mybatis.dynamic.sql.render.RenderingStrategies;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;
import java.util.Optional;

import static org.mybatis.dynamic.sql.SqlBuilder.isEqualTo;
import static org.mybatis.dynamic.sql.SqlBuilder.select;

/**
 * @author 吴庆龙
 * @date 2020/7/20 5:43 下午
 */
public class EmailAlarm implements JobAlarm {
    public static final Logger log = LoggerFactory.getLogger(EmailAlarm.class);

    @Override
    public boolean doAlarm(JobInfo jobInfo, JobLog jobLog) {
        boolean alarmResult = true;
        if (jobInfo == null || jobInfo.getAlarmEmail() == null || jobInfo.getAlarmEmail().trim().length() > 0) {
            return alarmResult;
        }

        String alarmContent = "Alarm Job LogId=" + jobInfo.getId();
        if (jobLog.getTriggerCode() != ResultT.SUCCESS_CODE) {
            alarmContent += "<br>TriggerMsg=<br>" + jobLog.getTriggerMsg();
        }
        if (jobLog.getExecCode() > 0 && jobLog.getExecCode() != ResultT.SUCCESS_CODE) {
            alarmContent += "<br>HandleCode=" + jobLog.getExecMsg();
        }

        // email 信息
        Optional<JobGroup> optionalJobGroup = AdminConfig.getInstance().getJobGroupMapper().selectOne(
                select(JobGroupDynamicSqlSupport.jobGroup.allColumns())
                        .from(JobGroupDynamicSqlSupport.jobGroup)
                        .where(JobGroupDynamicSqlSupport.name, isEqualTo(jobInfo.getGroupName()))
                        .build().render(RenderingStrategies.MYBATIS3)
        );
        String groupName = optionalJobGroup.isPresent() ? optionalJobGroup.get().getName() : "";
        String personal = "分布式任务调度平台";
        String title = "任务调度中心监控报警";
        String content = MessageFormat.format(loadEmailJobAlarmTemplate(),
                groupName, jobInfo.getId(), jobInfo.getDescription(), alarmContent);


        return false;
    }

    private static String loadEmailJobAlarmTemplate() {
        return "<h5>监控告警明细：</span>" +
                "<table border=\"1\" cellpadding=\"3\" style=\"border-collapse:collapse; width:80%;\" >\n" +
                "   <thead style=\"font-weight: bold;color: #ffffff;background-color: #ff8c00;\" >" +
                "      <tr>\n" +
                "         <td width=\"20%\" >执行器</td>\n" +
                "         <td width=\"10%\" >任务ID</td>\n" +
                "         <td width=\"20%\" >任务描述</td>\n" +
                "         <td width=\"10%\" >告警类型</td>\n" +
                "         <td width=\"40%\" >告警内容</td>\n" +
                "      </tr>\n" +
                "   </thead>\n" +
                "   <tbody>\n" +
                "      <tr>\n" +
                "         <td>{0}</td>\n" +
                "         <td>{1}</td>\n" +
                "         <td>{2}</td>\n" +
                "         <td>调度失败</td>\n" +
                "         <td>{3}</td>\n" +
                "      </tr>\n" +
                "   </tbody>\n" +
                "</table>";
    }

}
