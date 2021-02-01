package com.snailwu.job.admin.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.snailwu.job.admin.controller.request.JobLogDeleteRequest;
import com.snailwu.job.admin.controller.request.JobLogSearchRequest;
import com.snailwu.job.admin.controller.vo.JobLogVO;
import com.snailwu.job.admin.core.scheduler.JobScheduler;
import com.snailwu.job.admin.mapper.JobLogMapper;
import com.snailwu.job.admin.model.JobLog;
import com.snailwu.job.core.biz.ExecutorBiz;
import com.snailwu.job.core.biz.model.KillParam;
import com.snailwu.job.core.biz.model.ResultT;
import com.snailwu.job.core.exception.JobException;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.Date;

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
            throw new JobException("必须传结束时间");
        }
        return PageHelper.startPage(searchRequest.getPage(), searchRequest.getLimit())
                .doSelectPageInfo(() -> jobLogMapper.selectByCondition(searchRequest));
    }

    /**
     * 根据时间批量删除日志
     */
    public void deleteByTime(JobLogDeleteRequest dr) {
        jobLogMapper.deleteByCondition(dr);
    }

    /**
     * 终止任务
     */
    public ResultT<String> killTrigger(Long logId) {
        // 查询任务
        JobLog jobLog = jobLogMapper.selectByPrimaryKey(logId);
        if (jobLog == null) {
            throw new JobException("日志记录不存在");
        }
        // 执行中的任务有可能被终止
        if (jobLog.getExecCode() == 0) {
            ExecutorBiz executorBiz = JobScheduler.getOrCreateExecutorBiz(jobLog.getExecAddress());
            return executorBiz.kill(new KillParam(jobLog.getJobId(), logId));
        } else {
            return new ResultT<>(ResultT.FAIL_CODE, "任务已经执行完毕");
        }
    }
}
