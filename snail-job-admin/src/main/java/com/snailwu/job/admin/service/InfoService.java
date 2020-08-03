package com.snailwu.job.admin.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.snailwu.job.admin.constant.JobConstants;
import com.snailwu.job.admin.core.cron.CronExpression;
import com.snailwu.job.admin.core.model.JobInfo;
import com.snailwu.job.admin.mapper.JobInfoDynamicSqlSupport;
import com.snailwu.job.admin.mapper.JobInfoMapper;
import org.mybatis.dynamic.sql.render.RenderingStrategies;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.ParseException;
import java.util.Date;

import static org.mybatis.dynamic.sql.SqlBuilder.select;

/**
 * @author 吴庆龙
 * @date 2020/7/27 10:37 上午
 */
@Service
public class InfoService {
    private static final Logger LOGGER = LoggerFactory.getLogger(InfoService.class);

    @Resource
    private JobInfoMapper jobInfoMapper;

    /**
     * 开始运行任务
     * 任务在 {@link com.snailwu.job.admin.constant.JobConstants#PRE_LOAD_SLEEP_MS} 秒后生效
     */
    public void start(Integer id) {
        JobInfo jobInfo = jobInfoMapper.selectByPrimaryKey(id).orElse(null);
        if (jobInfo == null) {
            LOGGER.error("开始运行任务.无此任务:{}", id);
            return;
        }

        // 计算任务的下次执行时间
        CronExpression cronExpression;
        try {
            cronExpression = new CronExpression(jobInfo.getCron());
        } catch (ParseException e) {
            LOGGER.error("解析CRON表达式异常");
            return;
        }
        Date nextTimeAfter = new Date(System.currentTimeMillis() + JobConstants.PRE_LOAD_SLEEP_MS);
        Date triggerNextTime = cronExpression.getNextValidTimeAfter(nextTimeAfter);

        JobInfo updateJobInfo = new JobInfo();
        updateJobInfo.setId(id);
        updateJobInfo.setTriggerLastTime(0L);
        updateJobInfo.setTriggerNextTime(triggerNextTime.getTime());
        updateJobInfo.setTriggerStatus((byte) 1);
        jobInfoMapper.updateByPrimaryKeySelective(updateJobInfo);
    }

    /**
     * 停止任务
     */
    public void stop(Integer id) {
        JobInfo jobInfo = jobInfoMapper.selectByPrimaryKey(id).orElse(null);
        if (jobInfo == null) {
            LOGGER.error("开始运行任务.无此任务:{}", id);
            return;
        }

        JobInfo updateJobInfo = new JobInfo();
        updateJobInfo.setId(id);
        updateJobInfo.setTriggerLastTime(0L);
        updateJobInfo.setTriggerNextTime(0L);
        updateJobInfo.setTriggerStatus((byte) 0);
        jobInfoMapper.updateByPrimaryKeySelective(updateJobInfo);
    }

    /**
     * 分页查询
     */
    public PageInfo<JobInfo> list(Integer pageNum, Integer pageSize) {
        return PageHelper.startPage(pageNum, pageSize)
                .doSelectPageInfo(() -> {
                    jobInfoMapper.selectMany(
                            select(JobInfoDynamicSqlSupport.jobInfo.allColumns())
                            .from(JobInfoDynamicSqlSupport.jobInfo)
                            .build().render(RenderingStrategies.MYBATIS3)
                    );
                });
    }

    /**
     * 保存 或 更新
     */
    public void saveOrUpdate(JobInfo jobInfo) {

    }

    /**
     * 删除
     */
    public void delete(Integer id) {
        int ret = jobInfoMapper.deleteByPrimaryKey(id);
        if (ret == 1) {
            LOGGER.info("删除成功");
        } else {
            LOGGER.info("删除失败,记录不存在");
        }
    }

}
