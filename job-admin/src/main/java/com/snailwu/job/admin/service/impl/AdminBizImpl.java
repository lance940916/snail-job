package com.snailwu.job.admin.service.impl;

import com.snailwu.job.admin.mapper.JobExecutorMapper;
import com.snailwu.job.admin.mapper.JobLogMapper;
import com.snailwu.job.admin.model.JobExecutor;
import com.snailwu.job.admin.model.JobLog;
import com.snailwu.job.core.biz.AdminBiz;
import com.snailwu.job.core.biz.model.CallbackParam;
import com.snailwu.job.core.biz.model.RegistryParam;
import com.snailwu.job.core.biz.model.ResultT;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author 吴庆龙
 * @date 2020/6/3 11:34 上午
 */
@Service
public class AdminBizImpl implements AdminBiz {

    @Resource
    private JobExecutorMapper jobExecutorMapper;
    @Resource
    private JobLogMapper jobLogMapper;

    /**
     * 接收任务执行结果
     */
    @Override
    public ResultT<String> callback(CallbackParam callbackParam) {
        // 查询任务日志
        JobLog log = jobLogMapper.selectExecCodeById(callbackParam.getLogId());
        if (log == null) {
            return new ResultT<>(ResultT.FAIL_CODE, "无效的logId");
        }
        // execCode 只能在回调中进行更改
        if (log.getExecCode() != 0) {
            // FIXME 高并发重复回调会有问题
            return new ResultT<>(ResultT.FAIL_CODE, "重复回调");
        }

        // 更新 JobLog 执行结果
        JobLog updateLog = new JobLog();
        updateLog.setId(log.getId());
        updateLog.setExecTime(callbackParam.getExecTime());
        updateLog.setExecCode(callbackParam.getExecCode());
        updateLog.setExecMsg(callbackParam.getExecMsg());
        jobLogMapper.updateExecResultById(updateLog);
        return ResultT.SUCCESS;
    }

    @Override
    public ResultT<String> registry(RegistryParam param) {
        String appName = param.getAppName();
        String address = param.getAddress();
        JobExecutor executor = jobExecutorMapper.selectByAppNameAndAddress(appName, address);
        if (executor == null) {
            executor = new JobExecutor();
            executor.setAppName(appName);
            executor.setAddress(address);
            jobExecutorMapper.insert(executor);
        } else {
            executor.setUpdateTime(new Date());
            jobExecutorMapper.updateByPrimaryKey(executor);
        }
        return ResultT.SUCCESS;
    }

    @Override
    public ResultT<String> remove(RegistryParam param) {
        jobExecutorMapper.deleteByAppNameAndAddress(param.getAppName(), param.getAddress());
        return ResultT.SUCCESS;
    }
}
