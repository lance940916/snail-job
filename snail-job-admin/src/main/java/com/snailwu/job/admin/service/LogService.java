package com.snailwu.job.admin.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.snailwu.job.admin.controller.entity.JobLogVO;
import com.snailwu.job.admin.controller.request.JobLogDeleteRequest;
import com.snailwu.job.admin.controller.request.JobLogSearchRequest;
import com.snailwu.job.admin.core.model.JobLog;
import com.snailwu.job.admin.core.scheduler.SnailJobScheduler;
import com.snailwu.job.admin.mapper.JobInfoDynamicSqlSupport;
import com.snailwu.job.admin.mapper.JobLogDynamicSqlSupport;
import com.snailwu.job.admin.mapper.JobLogMapper;
import com.snailwu.job.core.biz.ExecutorBiz;
import com.snailwu.job.core.biz.model.KillParam;
import com.snailwu.job.core.biz.model.ResultT;
import com.snailwu.job.core.exception.SnailJobException;
import org.mybatis.dynamic.sql.render.RenderingStrategies;
import org.mybatis.dynamic.sql.select.render.SelectStatementProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

import static org.mybatis.dynamic.sql.SqlBuilder.*;

/**
 * @author 吴庆龙
 * @date 2020/7/27 10:37 上午
 */
@Service
public class LogService {
    private static final Logger LOGGER = LoggerFactory.getLogger(LogService.class);

    @Resource
    private JobLogMapper jobLogMapper;

    /**
     * 分页查询
     */
    public PageInfo<JobLogVO> list(JobLogSearchRequest searchRequest) {
        // 有开始触发时间必须有结束触发时间
        Date triggerBeginDate = searchRequest.getTriggerBeginDate();
        Date triggerEndDate = searchRequest.getTriggerEndDate();
        if (triggerBeginDate != null && triggerEndDate == null) {
            throw new SnailJobException("必须传结束时间");
        }

        SelectStatementProvider statementProvider = select(JobLogDynamicSqlSupport.jobLog.allColumns(), JobInfoDynamicSqlSupport.name)
                .from(JobLogDynamicSqlSupport.jobLog)
                .leftJoin(JobInfoDynamicSqlSupport.jobInfo).on(JobInfoDynamicSqlSupport.id, equalTo(JobLogDynamicSqlSupport.jobId))
                .where()
                .and(JobLogDynamicSqlSupport.groupName, isEqualToWhenPresent(searchRequest.getGroupName()))
                .and(JobLogDynamicSqlSupport.jobId, isEqualToWhenPresent(searchRequest.getJobId()))
                .and(JobLogDynamicSqlSupport.triggerCode, isEqualToWhenPresent(searchRequest.getTriggerCode()))
                .and(JobLogDynamicSqlSupport.execCode, isEqualToWhenPresent(searchRequest.getExecCode()))
                .and(JobLogDynamicSqlSupport.triggerTime, isBetweenWhenPresent(triggerBeginDate).and(triggerEndDate))
                .orderBy(JobLogDynamicSqlSupport.triggerTime.descending())
                .build().render(RenderingStrategies.MYBATIS3);
        return PageHelper.startPage(searchRequest.getPage(), searchRequest.getLimit())
                .doSelectPageInfo(() -> jobLogMapper.selectManyWithJobName(statementProvider));
    }

    /**
     * 根据时间批量删除日志
     */
    public void deleteByTime(JobLogDeleteRequest deleteRequest) {
        jobLogMapper.delete(
                deleteFrom(JobLogDynamicSqlSupport.jobLog)
                        .where()
                        .and(JobLogDynamicSqlSupport.groupName, isEqualToWhenPresent(deleteRequest.getGroupName()))
                        .and(JobLogDynamicSqlSupport.jobId, isEqualToWhenPresent(deleteRequest.getJobId()))
                        .and(JobLogDynamicSqlSupport.triggerTime, isBetween(deleteRequest.getBeginDate()).and(deleteRequest.getEndDate()))
                        .build().render(RenderingStrategies.MYBATIS3)
        );
    }

    /**
     * 终止任务
     */
    public ResultT<String> killTrigger(Long logId) {
        // 查询任务
        JobLog jobLog = jobLogMapper.selectOne(
                select(JobLogDynamicSqlSupport.id, JobLogDynamicSqlSupport.jobId, JobLogDynamicSqlSupport.execCode,
                        JobLogDynamicSqlSupport.executorAddress)
                        .from(JobLogDynamicSqlSupport.jobLog)
                        .where(JobLogDynamicSqlSupport.id, isEqualTo(logId))
                        .build().render(RenderingStrategies.MYBATIS3)
        ).orElse(null);
        if (jobLog == null) {
            throw new SnailJobException("Log记录不存在");
        }
        if (jobLog.getExecCode() == 0) {
            ExecutorBiz executorBiz = SnailJobScheduler.getExecutorBiz(jobLog.getExecutorAddress());
            return executorBiz.kill(new KillParam(jobLog.getJobId(), logId));
        } else {
            return new ResultT<>(ResultT.FAIL_CODE, "任务已经执行完毕");
        }
    }
}
