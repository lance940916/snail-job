package com.snailwu.job.admin.trigger;

import com.snailwu.job.admin.core.conf.AdminConfig;
import com.snailwu.job.admin.core.model.JobGroup;
import com.snailwu.job.admin.core.model.JobInfo;
import com.snailwu.job.admin.core.model.JobLog;
import com.snailwu.job.admin.core.route.ExecutorRouteStrategyEnum;
import com.snailwu.job.admin.core.scheduler.SnailJobScheduler;
import com.snailwu.job.admin.mapper.JobGroupDynamicSqlSupport;
import com.snailwu.job.admin.mapper.JobInfoDynamicSqlSupport;
import com.snailwu.job.core.biz.ExecutorBiz;
import com.snailwu.job.core.biz.model.ResultT;
import com.snailwu.job.core.biz.model.TriggerParam;
import org.mybatis.dynamic.sql.render.RenderingStrategies;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.mybatis.dynamic.sql.SqlBuilder.isEqualTo;
import static org.mybatis.dynamic.sql.SqlBuilder.select;

/**
 * 指定任务进行调度
 *
 * @author 吴庆龙
 * @date 2020/6/17 1:56 下午
 */
public class JobTrigger {
    private static final Logger log = LoggerFactory.getLogger(JobTrigger.class);

    /**
     * 触发 Job
     */
    public static void trigger(int jobId, int failRetryCount, String executorParam) {
        // 查询任务信息
        Optional<JobInfo> jobInfoOptional = AdminConfig.getInstance().getJobInfoMapper().selectOne(
                select(JobInfoDynamicSqlSupport.jobInfo.allColumns())
                        .from(JobInfoDynamicSqlSupport.jobInfo)
                        .where(JobInfoDynamicSqlSupport.id, isEqualTo(jobId))
                        .build().render(RenderingStrategies.MYBATIS3)
        );
        if (!jobInfoOptional.isPresent()) {
            log.error("jobId:{} 无此数据", jobId);
            return;
        }
        JobInfo jobInfo = jobInfoOptional.get();
        if (executorParam != null) {
            jobInfo.setExecutorParam(executorParam);
        }

        // 查询任务分组信息
        Optional<JobGroup> jobGroupOptional = AdminConfig.getInstance().getJobGroupMapper().selectOne(
                select(JobGroupDynamicSqlSupport.jobGroup.addressList)
                        .from(JobGroupDynamicSqlSupport.jobGroup)
                        .where(JobGroupDynamicSqlSupport.name, isEqualTo(jobInfo.getGroupName()))
                        .build().render(RenderingStrategies.MYBATIS3)
        );
        if (!jobGroupOptional.isPresent()) {
            log.warn("执行器不存在. {}", jobInfo.getGroupName());
            return;
        }
        JobGroup jobGroup = jobGroupOptional.get();
        String addresses = jobGroup.getAddressList();

        // 失败重试次数
        int finalFailRetryCount = failRetryCount > 0 ? failRetryCount : jobInfo.getExecutorFailRetryCount();

        // 处理
        processTrigger(addresses, jobInfo, finalFailRetryCount);
    }

    private static void processTrigger(String addresses, JobInfo jobInfo, int finalFailRetryCount) {
        ExecutorRouteStrategyEnum routeStrategy = ExecutorRouteStrategyEnum.match(jobInfo.getExecutorRouteStrategy());

        // 1 保存日志
        JobLog jobLog = new JobLog();
        jobLog.setGroupName(jobInfo.getGroupName());
        jobLog.setJobId(jobInfo.getId());
        jobLog.setTriggerTime(new Date());
        AdminConfig.getInstance().getJobLogMapper().insertSelective(jobLog);
        log.info("保存任务日志。jobId:{}", jobLog.getJobId());

        // 2 初始化触发参数
        TriggerParam triggerParam = new TriggerParam();
        triggerParam.setJobId(jobInfo.getId());
        triggerParam.setExecutorHandler(jobInfo.getExecutorHandler());
        triggerParam.setExecutorParams(jobInfo.getExecutorParam());
        triggerParam.setExecutorBlockStrategy(jobInfo.getExecutorBlockStrategy());
        triggerParam.setExecutorTimeout(jobInfo.getExecutorTimeout());
        triggerParam.setLogId(jobLog.getId());
        triggerParam.setLogDateTime(jobLog.getTriggerTime().getTime());

        // 3 选择一个执行器
        String address = null;
        if (addresses != null) {
            List<String> addressList = Arrays.asList(addresses.split(","));
            ResultT<String> routeAddressResult = routeStrategy.getRouter().route(triggerParam, addressList);
            if (routeAddressResult.getCode() == ResultT.SUCCESS_CODE) {
                address = routeAddressResult.getContent();
            }
        }

        // 4 触发远程执行器
        ResultT<String> triggerResult;
        if (address != null) {
            triggerResult = runExecutor(triggerParam, address);
        } else {
            triggerResult = new ResultT<>(ResultT.FAIL_CODE, "未找到执行器地址");
        }

        // 5 更新 Log
        JobLog updateJobLog = new JobLog();
        updateJobLog.setId(jobLog.getId());
        updateJobLog.setGroupName(jobInfo.getGroupName());
        updateJobLog.setExecutorAddress(address);
        updateJobLog.setExecutorHandler(jobInfo.getExecutorHandler());
        updateJobLog.setExecutorParam(jobInfo.getExecutorParam());
        updateJobLog.setExecutorFailRetryCount((byte) finalFailRetryCount);
        updateJobLog.setTriggerTime(new Date());
        updateJobLog.setTriggerCode(triggerResult.getCode());
        updateJobLog.setTriggerMsg(triggerResult.getMsg());
        AdminConfig.getInstance().getJobLogMapper().updateByPrimaryKeySelective(updateJobLog);
    }

    /**
     * 使用执行器执行任务
     */
    private static ResultT<String> runExecutor(TriggerParam triggerParam, String address) {
        ResultT<String> result;
        try {
            ExecutorBiz executorBiz = SnailJobScheduler.getExecutorBiz(address);
            result = executorBiz.run(triggerParam);
        } catch (Exception e) {
            log.error("trigger error, please check if the executor[{}] is running.", address, e);
            result = new ResultT<>(ResultT.FAIL_CODE, e.getMessage());
        }
        log.info("************* 执行结果 *************");
        log.info("address: {}", address);
        log.info("code: {}", result.getCode());
        log.info("msg: {}", result.getMsg());
        log.info("***********************************");
        return result;
    }

}
