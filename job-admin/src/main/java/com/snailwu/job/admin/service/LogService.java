//package com.snailwu.job.admin.service;
//
//import com.github.pagehelper.PageHelper;
//import com.github.pagehelper.PageInfo;
//import com.snailwu.job.admin.controller.request.JobLogDeleteRequest;
//import com.snailwu.job.admin.controller.request.JobLogSearchRequest;
//import com.snailwu.job.admin.controller.vo.JobLogVO;
//import com.snailwu.job.admin.core.scheduler.JobScheduler;
//import com.snailwu.job.admin.mapper.JobInfoDynamicSqlSupport;
//import com.snailwu.job.admin.mapper.JobLogDynamicSqlSupport;
//import com.snailwu.job.admin.mapper.JobLogMapper;
//import com.snailwu.job.admin.mapper.custom.CustomJobLogMapper;
//import com.snailwu.job.admin.model.JobLog;
//import com.snailwu.job.core.biz.ExecutorBiz;
//import com.snailwu.job.core.biz.model.KillParam;
//import com.snailwu.job.core.biz.model.ResultT;
//import com.snailwu.job.core.exception.JobException;
//import org.mybatis.dynamic.sql.render.RenderingStrategies;
//import org.mybatis.dynamic.sql.select.render.SelectStatementProvider;
//import org.springframework.stereotype.Service;
//
//import javax.annotation.Resource;
//import java.util.Date;
//
//import static org.mybatis.dynamic.sql.SqlBuilder.*;
//
///**
// * @author 吴庆龙
// * @date 2020/7/27 10:37 上午
// */
//@Service
//public class LogService {
//
//    @Resource
//    private JobLogMapper jobLogMapper;
//    @Resource
//    private CustomJobLogMapper customJobLogMapper;
//
//    /**
//     * 分页查询
//     */
//    public PageInfo<JobLogVO> list(JobLogSearchRequest searchRequest) {
//        // 有开始触发时间必须有结束触发时间
//        Date triggerBeginDate = searchRequest.getTriggerBeginDate();
//        Date triggerEndDate = searchRequest.getTriggerEndDate();
//        if (triggerBeginDate != null && triggerEndDate == null) {
//            throw new JobException("必须传结束时间");
//        }
//
//        SelectStatementProvider statementProvider =
//                select(JobLogDynamicSqlSupport.jobLog.allColumns(), JobInfoDynamicSqlSupport.name.as("job_name"))
//                .from(JobLogDynamicSqlSupport.jobLog)
//                .leftJoin(JobInfoDynamicSqlSupport.jobInfo)
//                .on(JobInfoDynamicSqlSupport.id, equalTo(JobLogDynamicSqlSupport.jobId))
//                .where()
//                .and(JobLogDynamicSqlSupport.appName, isEqualToWhenPresent(searchRequest.getGroupName()))
//                .and(JobLogDynamicSqlSupport.jobId, isEqualToWhenPresent(searchRequest.getJobId()))
//                .and(JobLogDynamicSqlSupport.triggerCode, isEqualToWhenPresent(searchRequest.getTriggerCode()))
//                .and(JobLogDynamicSqlSupport.execCode, isEqualToWhenPresent(searchRequest.getExecCode()))
//                .and(JobLogDynamicSqlSupport.triggerTime, isBetweenWhenPresent(triggerBeginDate).and(triggerEndDate))
//                .orderBy(JobLogDynamicSqlSupport.triggerTime.descending())
//                .build().render(RenderingStrategies.MYBATIS3);
//
//        return PageHelper.startPage(searchRequest.getPage(), searchRequest.getLimit())
//                .doSelectPageInfo(() -> customJobLogMapper.selectManyWithJobName(statementProvider));
//    }
//
//    /**
//     * 根据时间批量删除日志
//     */
//    public void deleteByTime(JobLogDeleteRequest dr) {
//        jobLogMapper.delete(
//                deleteFrom(JobLogDynamicSqlSupport.jobLog)
//                        .where()
//                        .and(JobLogDynamicSqlSupport.appName, isEqualToWhenPresent(dr.getGroupName()))
//                        .and(JobLogDynamicSqlSupport.jobId, isEqualToWhenPresent(dr.getJobId()))
//                        .and(JobLogDynamicSqlSupport.triggerTime, isBetween(dr.getBeginDate()).and(dr.getEndDate()))
//                        .build().render(RenderingStrategies.MYBATIS3)
//        );
//    }
//
//    /**
//     * 终止任务
//     */
//    public ResultT<String> killTrigger(Long logId) {
//        // 查询任务
//        JobLog jobLog = jobLogMapper.selectOne(
//                select(JobLogDynamicSqlSupport.id, JobLogDynamicSqlSupport.jobId, JobLogDynamicSqlSupport.execCode,
//                        JobLogDynamicSqlSupport.execAddress)
//                        .from(JobLogDynamicSqlSupport.jobLog)
//                        .where(JobLogDynamicSqlSupport.id, isEqualTo(logId))
//                        .build().render(RenderingStrategies.MYBATIS3)
//        ).orElse(null);
//        if (jobLog == null) {
//            throw new JobException("Log记录不存在");
//        }
//        if (jobLog.getExecCode() == 0) {
//            ExecutorBiz executorBiz = JobScheduler.obtainOrCreateExecutorBiz(jobLog.getExecAddress());
//            return executorBiz.kill(new KillParam(jobLog.getJobId(), logId));
//        } else {
//            return new ResultT<>(ResultT.FAIL_CODE, "任务已经执行完毕");
//        }
//    }
//}
