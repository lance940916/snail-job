package com.snailwu.job.admin.service;

import com.github.pagehelper.PageInfo;
import com.github.pagehelper.page.PageMethod;
import com.snailwu.job.admin.constant.AdminConstants;
import com.snailwu.job.admin.controller.request.JobInfoSearchRequest;
import com.snailwu.job.admin.core.cron.CronExpression;
import com.snailwu.job.admin.mapper.JobInfoMapper;
import com.snailwu.job.admin.model.JobInfo;
import com.snailwu.job.core.exception.JobException;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import static com.snailwu.job.core.enums.TriggerStatus.RUNNING;
import static com.snailwu.job.core.enums.TriggerStatus.STOPPED;

/**
 * @author 吴庆龙
 * @date 2020/7/27 10:37 上午
 */
@Service
public class InfoService {
    private static final Logger logger = LoggerFactory.getLogger(InfoService.class);

    @Resource
    private JobInfoMapper jobInfoMapper;

    /**
     * 开始运行任务 任务在 {@link AdminConstants#SCAN_JOB_SLEEP_MS} 秒后生效
     */
    public void start(Integer id) {
        JobInfo jobInfo = jobInfoMapper.selectByPrimaryKey(id);
        if (jobInfo == null) {
            logger.error("开始运行任务.无此任务:{}", id);
            return;
        }

        // 计算任务的下次执行时间
        CronExpression cronExpression;
        try {
            cronExpression = new CronExpression(jobInfo.getCron());
        } catch (ParseException e) {
            logger.error("解析CRON表达式异常");
            return;
        }
        Date nextTimeAfter = new Date(System.currentTimeMillis() + AdminConstants.SCAN_JOB_SLEEP_MS);
        Date triggerNextDate = cronExpression.getNextValidTimeAfter(nextTimeAfter);
        long triggerNextTime = triggerNextDate.getTime() / 1000;
        jobInfoMapper.updateNextTimeById(0L, triggerNextTime, RUNNING.getValue(), id);
    }

    /**
     * 停止任务
     */
    public void stop(Integer id) {
        JobInfo jobInfo = jobInfoMapper.selectByPrimaryKey(id);
        if (jobInfo == null) {
            logger.error("开始运行任务.无此任务:{}", id);
            return;
        }
        jobInfoMapper.updateNextTimeById(0L, 0L, STOPPED.getValue(), id);
    }

    /**
     * 分页查询
     */
    public PageInfo<JobInfo> list(JobInfoSearchRequest searchRequest) {
        return PageMethod.startPage(searchRequest.getPage(), searchRequest.getLimit())
                .doSelectPageInfo(() -> jobInfoMapper.selectByCondition(searchRequest));
    }

    /**
     * 保存 或 更新
     */
    public void saveOrUpdate(JobInfo info) {
        if (info.getId() == null) {
            // Cron 表达式是否正确
            Validate.isTrue(CronExpression.isValidExpression(info.getCron()), "Cron表达式不正确");
            jobInfoMapper.insert(info);
        } else {
            // 更新
            info.setUpdateTime(new Date());
            jobInfoMapper.updateByPrimaryKey(info);
        }
    }

    /**
     * 删除
     */
    public void delete(Integer id) {
        jobInfoMapper.deleteByPrimaryKey(id);
    }

    /**
     * 复制一个任务
     */
    public void copy(Integer id) {
        JobInfo jobInfo = jobInfoMapper.selectByPrimaryKey(id);
        if (jobInfo == null) {
            throw new JobException("任务不存在");
        }
        jobInfo.setId(null);
        jobInfo.setAppName(jobInfo.getAppName() + "（复制）");
        jobInfo.setTriggerStatus(STOPPED.getValue());
        jobInfo.setTriggerLastTime(0L);
        jobInfo.setTriggerNextTime(0L);
        jobInfo.setCreateTime(null);
        jobInfo.setUpdateTime(null);
        jobInfoMapper.insert(jobInfo);
    }

    /**
     * 列出分组下的所有任务
     */
    public List<JobInfo> listAll(String appName) {
        return jobInfoMapper.selectByAppName(appName);
    }
}
