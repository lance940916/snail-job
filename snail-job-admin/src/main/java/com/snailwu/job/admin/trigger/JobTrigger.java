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
 * 进行任务的调度
 *
 * @author 吴庆龙
 * @date 2020/6/17 1:56 下午
 */
public class JobTrigger {
    private static final Logger log = LoggerFactory.getLogger(JobTrigger.class);

    /**
     * 触发 Job
     *
     * @param executorParam 执行参数，手动执行时页面填写的会覆盖任务中的参数
     */
    public static void trigger(int jobId, TriggerTypeEnum triggerType, int failRetryCount, String executorParam) {
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

        // 任务调度地址集合
        String groupAddresses = jobGroup.getAddressList();

        // 失败重试次数
        int finalFailRetryCount = failRetryCount >= 0 ? failRetryCount : jobInfo.getExecutorFailRetryCount();

        // 进行调度
        processTrigger(groupAddresses, jobInfo, triggerType, finalFailRetryCount);
    }

    /**
     * 进行调度
     */
    private static void processTrigger(String groupAddresses, JobInfo jobInfo, TriggerTypeEnum triggerType,
                                       int failRetryCount) {
        log.info("调度类型: {}", triggerType);

        // 调度时间
        Date triggerTime = new Date();
        // 执行器路由策略
        ExecutorRouteStrategyEnum routeStrategy = ExecutorRouteStrategyEnum.match(jobInfo.getExecutorRouteStrategy());

        // 1 保存日志
        JobLog jobLog = new JobLog();
        jobLog.setJobId(jobInfo.getId());
        jobLog.setGroupName(jobInfo.getGroupName());
        jobLog.setTriggerTime(triggerTime);
        AdminConfig.getInstance().getJobLogMapper().insertSelective(jobLog);
        log.info("保存任务执行日志.jobId:{}", jobLog.getJobId());

        // 2 初始化触发参数
        TriggerParam triggerParam = new TriggerParam();
        triggerParam.setJobId(jobInfo.getId());
        triggerParam.setExecutorHandler(jobInfo.getExecutorHandler());
        triggerParam.setExecutorParams(jobInfo.getExecutorParam());
        triggerParam.setExecutorBlockStrategy(jobInfo.getExecutorBlockStrategy());
        triggerParam.setExecutorTimeout(jobInfo.getExecutorTimeout());
        triggerParam.setLogId(jobLog.getId());

        // 3 选择一个执行器
        String address = null;
        if (groupAddresses != null) {
            List<String> addressList = Arrays.asList(groupAddresses.split(","));
            ResultT<String> routeAddressResult = routeStrategy.getRouter().route(triggerParam, addressList);
            if (routeAddressResult.getCode() == ResultT.SUCCESS_CODE) {
                address = routeAddressResult.getContent();
            }
        }
        log.info("任务执行地址:{}", address);

        // 4 触发远程执行器
        ResultT<String> triggerResult;
        if (address != null) {
            triggerResult = runExecutor(triggerParam, address);
        } else {
            triggerResult = new ResultT<>(ResultT.FAIL_CODE, "未找到执行器地址");
        }

        // 5 更新 Log
        JobLog updateJobLog = new JobLog();
        // 执行相关字段
        updateJobLog.setExecutorAddress(address);
        updateJobLog.setExecutorHandler(jobInfo.getExecutorHandler());
        updateJobLog.setExecutorParam(jobInfo.getExecutorParam());
        updateJobLog.setExecutorFailRetryCount((byte) failRetryCount);
        // 调度相关字段
        updateJobLog.setTriggerTime(triggerTime);
        updateJobLog.setTriggerCode(triggerResult.getCode());
        updateJobLog.setTriggerMsg(triggerResult.getMsg());
        AdminConfig.getInstance().getJobLogMapper().updateByPrimaryKeySelective(updateJobLog);
        log.info("更新任务执行结果成功");
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
            log.error("调度异常,调度的执行器:{}.", address);
            result = new ResultT<>(ResultT.FAIL_CODE, e.getMessage());
        }
        log.info("************* 调度结果 *************");
        log.info("调度地址: {}", address);
        log.info("调度返回码: {}", result.getCode());
        log.info("调度返回消息: {}", result.getMsg());
        log.info("调度返回内容: {}", result.getContent());
        log.info("***********************************");
        return result;
    }

}
