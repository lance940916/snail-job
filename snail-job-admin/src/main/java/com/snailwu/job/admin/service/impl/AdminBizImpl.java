package com.snailwu.job.admin.service.impl;

import com.snailwu.job.admin.mapper.JobLogDynamicSqlSupport;
import com.snailwu.job.admin.mapper.JobLogMapper;
import com.snailwu.job.admin.mapper.JobNodeDynamicSqlSupport;
import com.snailwu.job.admin.mapper.JobNodeMapper;
import com.snailwu.job.admin.model.JobLog;
import com.snailwu.job.admin.model.JobNode;
import com.snailwu.job.core.biz.AdminBiz;
import com.snailwu.job.core.biz.model.CallbackParam;
import com.snailwu.job.core.biz.model.RegistryParam;
import com.snailwu.job.core.biz.model.ResultT;
import org.mybatis.dynamic.sql.render.RenderingStrategies;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

import static org.mybatis.dynamic.sql.SqlBuilder.*;

/**
 * @author 吴庆龙
 * @date 2020/6/3 11:34 上午
 */
@Service
public class AdminBizImpl implements AdminBiz {

    @Resource
    private JobNodeMapper jobExecutorMapper;
    @Resource
    private JobLogMapper jobLogMapper;

    /**
     * 接收任务执行结果
     */
    @Override
    public ResultT<String> callback(CallbackParam callbackParam) {
        // 查询任务日志
        JobLog jobLog = jobLogMapper.selectOne(
                select(JobLogDynamicSqlSupport.id, JobLogDynamicSqlSupport.execCode)
                        .from(JobLogDynamicSqlSupport.jobLog)
                        .where(JobLogDynamicSqlSupport.id, isEqualTo(callbackParam.getLogId()))
                        .build().render(RenderingStrategies.MYBATIS3)
        ).orElse(null);
        if (jobLog == null) {
            return new ResultT<>(ResultT.FAIL_CODE, "无效的logId");
        }
        if (jobLog.getExecCode() != 0) {
            return new ResultT<>(ResultT.FAIL_CODE, "重复回调");
        }

        // 更新 JobLog 执行结果
        JobLog updateJobLog = new JobLog();
        updateJobLog.setId(jobLog.getId());
        updateJobLog.setExecTime(callbackParam.getExecTime());
        updateJobLog.setExecCode(callbackParam.getExecCode());
        updateJobLog.setExecMsg(callbackParam.getExecMsg());
        jobLogMapper.updateByPrimaryKeySelective(updateJobLog);
        return ResultT.SUCCESS;
    }

    @Override
    public ResultT<String> registry(RegistryParam registryParam) {
        String appName = registryParam.getAppName();
        String address = registryParam.getAddress();
        JobNode jobExecutor = jobExecutorMapper.selectOne(
                select(JobNodeDynamicSqlSupport.id)
                        .from(JobNodeDynamicSqlSupport.jobNode)
                        .where(JobNodeDynamicSqlSupport.appName, isEqualTo(appName))
                        .and(JobNodeDynamicSqlSupport.address, isEqualTo(address))
                        .build().render(RenderingStrategies.MYBATIS3)
        ).orElse(null);
        if (jobExecutor == null) {
            // 新增
            jobExecutor = new JobNode();
            jobExecutor.setAppName(appName);
            jobExecutor.setAddress(address);
            jobExecutor.setUpdateTime(new Date());
            jobExecutorMapper.insertSelective(jobExecutor);
        } else {
            // 更新 update_time
            jobExecutor.setUpdateTime(new Date());
            jobExecutorMapper.updateByPrimaryKeySelective(jobExecutor);
        }
        return ResultT.SUCCESS;
    }

    @Override
    public ResultT<String> registryRemove(RegistryParam registryParam) {
        // 直接删除
        jobExecutorMapper.delete(
                deleteFrom(JobNodeDynamicSqlSupport.jobNode)
                        .where(JobNodeDynamicSqlSupport.appName,
                                isEqualTo(registryParam.getAppName()))
                        .and(JobNodeDynamicSqlSupport.address,
                                isEqualTo(registryParam.getAddress()))
                        .build().render(RenderingStrategies.MYBATIS3)
        );
        return ResultT.SUCCESS;
    }
}
