package com.snailwu.job.admin.service;

import com.github.pagehelper.PageInfo;
import com.github.pagehelper.page.PageMethod;
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
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

import static com.snailwu.job.admin.mapper.JobLogDynamicSqlSupport.*;
import static org.mybatis.dynamic.sql.SqlBuilder.*;

/**
 * @author 吴庆龙
 * @date 2020/7/27 10:37 上午
 */
@Service
public class LogService {

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

        SelectStatementProvider statementProvider = select(jobLog.allColumns(), JobInfoDynamicSqlSupport.name)
                .from(jobLog)
                .leftJoin(JobInfoDynamicSqlSupport.jobInfo).on(JobInfoDynamicSqlSupport.id, equalTo(jobId))
                .where()
                .and(groupName, isEqualToWhenPresent(searchRequest.getGroupName()))
                .and(jobId, isEqualToWhenPresent(searchRequest.getJobId()))
                .and(triggerCode, isEqualToWhenPresent(searchRequest.getTriggerCode()))
                .and(execCode, isEqualToWhenPresent(searchRequest.getExecCode()))
                .and(triggerTime, isBetweenWhenPresent(triggerBeginDate).and(triggerEndDate))
                .orderBy(triggerTime.descending())
                .build().render(RenderingStrategies.MYBATIS3);
        return PageMethod.startPage(searchRequest.getPage(), searchRequest.getLimit())
                .doSelectPageInfo(() -> jobLogMapper.selectManyWithJobName(statementProvider));
    }

    /**
     * 根据时间批量删除日志
     */
    public void deleteByTime(JobLogDeleteRequest dr) {
        jobLogMapper.delete(
                deleteFrom(jobLog)
                        .where()
                        .and(groupName, isEqualToWhenPresent(dr.getGroupName()))
                        .and(jobId, isEqualToWhenPresent(dr.getJobId()))
                        .and(triggerTime, isBetween(dr.getBeginDate()).and(dr.getEndDate()))
                        .build().render(RenderingStrategies.MYBATIS3)
        );
    }

    /**
     * 终止任务
     */
    public ResultT<String> killTrigger(Long logId) {
        // 查询任务
        JobLog jobLog = jobLogMapper.selectOne(
                select(id, jobId, execCode, executorAddress)
                        .from(JobLogDynamicSqlSupport.jobLog)
                        .where(id, isEqualTo(logId))
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
