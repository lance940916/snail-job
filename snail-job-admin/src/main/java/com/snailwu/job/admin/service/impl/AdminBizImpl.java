package com.snailwu.job.admin.service.impl;

import com.snailwu.job.admin.core.conf.AdminConfig;
import com.snailwu.job.admin.core.model.JobExecutor;
import com.snailwu.job.admin.core.model.JobLog;
import com.snailwu.job.admin.mapper.JobExecutorDynamicSqlSupport;
import com.snailwu.job.admin.mapper.JobExecutorMapper;
import com.snailwu.job.admin.mapper.JobLogDynamicSqlSupport;
import com.snailwu.job.admin.mapper.JobLogMapper;
import com.snailwu.job.core.biz.AdminBiz;
import com.snailwu.job.core.biz.model.CallbackParam;
import com.snailwu.job.core.biz.model.RegistryParam;
import com.snailwu.job.core.biz.model.ResultT;
import com.snailwu.job.core.enums.RegistryType;
import org.mybatis.dynamic.sql.render.RenderingStrategies;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

import static org.mybatis.dynamic.sql.SqlBuilder.*;

/**
 * @author 吴庆龙
 * @date 2020/6/3 11:34 上午
 */
@Service
public class AdminBizImpl implements AdminBiz {
    private static final Logger log = LoggerFactory.getLogger(AdminBizImpl.class);

    @Resource
    private JobExecutorMapper jobExecutorMapper;
    @Resource
    private JobLogMapper jobLogMapper;

    /**
     * 接收任务执行结果
     */
    @Override
    public ResultT<String> callback(List<CallbackParam> callbackParamList) {
        for (CallbackParam callbackParam : callbackParamList) {
            // 查询任务日志
            JobLog jobLog = jobLogMapper.selectOne(
                    select(JobLogDynamicSqlSupport.jobLog.allColumns())
                            .from(JobLogDynamicSqlSupport.jobLog)
                            .where(JobLogDynamicSqlSupport.id, isEqualTo(callbackParam.getLogId()))
                            .build().render(RenderingStrategies.MYBATIS3)
            ).orElse(null);
            if (jobLog == null) {
                return new ResultT<>(ResultT.FAIL_CODE, "没有对应的任务Log记录");
            }
            if (jobLog.getExecCode() != null) {
                return new ResultT<>(ResultT.FAIL_CODE, "重复回调");
            }

            // 更新 JobLog 执行结果
            JobLog updateJobLog = new JobLog();
            updateJobLog.setId(jobLog.getId());
            updateJobLog.setExecTime(callbackParam.getExecTime());
            updateJobLog.setExecCode(callbackParam.getExecCode());
            updateJobLog.setExecMsg(callbackParam.getExecMsg());
            AdminConfig.getInstance().getJobLogMapper().updateByPrimaryKeySelective(updateJobLog);
            log.info("回调成功. 任务执行结果: {}", callbackParam.getExecCode());
        }
        return ResultT.SUCCESS;
    }

    @Override
    public ResultT<String> registry(RegistryParam registryParam) {
        JobExecutor jobExecutor = jobExecutorMapper.selectOne(
                select(JobExecutorDynamicSqlSupport.id)
                        .from(JobExecutorDynamicSqlSupport.jobExecutor)
                        .where(JobExecutorDynamicSqlSupport.groupName, isEqualTo(registryParam.getGroupName()))
                        .and(JobExecutorDynamicSqlSupport.address, isEqualTo(registryParam.getExecutorAddress()))
                        .build().render(RenderingStrategies.MYBATIS3)
        ).orElse(null);
        if (jobExecutor == null) {
            // insert
            JobExecutor executor = new JobExecutor();
            executor.setGroupName(registryParam.getGroupName());
            executor.setAddress(registryParam.getExecutorAddress());
            executor.setRegistryType(RegistryType.AUTO.getValue());
            executor.setUpdateTime(new Date());
            jobExecutorMapper.insertSelective(executor);
        } else {
            // update
            JobExecutor executor = new JobExecutor();
            executor.setId(jobExecutor.getId());
            executor.setUpdateTime(new Date());
            jobExecutorMapper.updateByPrimaryKeySelective(executor);
        }
        return ResultT.SUCCESS;
    }

    @Override
    public ResultT<String> registryRemove(RegistryParam registryParam) {
        // 直接删除
        jobExecutorMapper.delete(
                deleteFrom(JobExecutorDynamicSqlSupport.jobExecutor)
                        .where(JobExecutorDynamicSqlSupport.groupName, isEqualTo(registryParam.getGroupName()))
                        .and(JobExecutorDynamicSqlSupport.address, isEqualTo(registryParam.getExecutorAddress()))
                        .build().render(RenderingStrategies.MYBATIS3)
        );
        return ResultT.SUCCESS;
    }
}
