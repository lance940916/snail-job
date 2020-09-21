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
import org.apache.commons.lang3.time.DateFormatUtils;
import org.mybatis.dynamic.sql.render.RenderingStrategies;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static com.snailwu.job.admin.constant.JobConstants.DATE_TIME_MS_PATTERN;
import static org.mybatis.dynamic.sql.SqlBuilder.isEqualTo;
import static org.mybatis.dynamic.sql.SqlBuilder.select;

/**
 * 进行任务的调度
 *
 * @author 吴庆龙
 * @date 2020/6/17 1:56 下午
 */
public class JobTrigger {
    private static final Logger LOGGER = LoggerFactory.getLogger(JobTrigger.class);

    private JobTrigger() {
    }

    /**
     * 触发 Job
     *
     * @param executorParam 执行参数，手动执行时页面填写的会覆盖任务中的参数
     */
    public static void trigger(int jobId, TriggerTypeEnum triggerType, int failRetryCount,
                               String executorParam) {
        // 查询任务信息
        JobInfo jobInfo = AdminConfig.getInstance().getJobInfoMapper().selectOne(
                select(JobInfoDynamicSqlSupport.jobInfo.allColumns())
                        .from(JobInfoDynamicSqlSupport.jobInfo)
                        .where(JobInfoDynamicSqlSupport.id, isEqualTo(jobId))
                        .build().render(RenderingStrategies.MYBATIS3)
        ).orElse(null);
        if (jobInfo == null) {
            LOGGER.error("无效的任务ID:{}", jobId);
            return;
        }
        if (executorParam != null) {
            jobInfo.setExecutorParam(executorParam);
        }

        // 查询任务分组信息
        String groupName = jobInfo.getGroupName();
        JobGroup jobGroup = AdminConfig.getInstance().getJobGroupMapper().selectOne(
                select(JobGroupDynamicSqlSupport.id, JobGroupDynamicSqlSupport.addressList)
                        .from(JobGroupDynamicSqlSupport.jobGroup)
                        .where(JobGroupDynamicSqlSupport.name, isEqualTo(groupName))
                        .build().render(RenderingStrategies.MYBATIS3)
        ).orElse(null);
        if (jobGroup == null) {
            LOGGER.error("任务分组信息不存在.name:{}", groupName);
            return;
        }

        // 任务调度地址集合
        String groupAddresses = jobGroup.getAddressList();

        // 失败重试次数（如果参数大于，则使用参数的值）
        int finalFailRetryCount = failRetryCount >= 0 ?
                failRetryCount : jobInfo.getExecutorFailRetryCount();

        // 进行调度
        processTrigger(jobInfo, groupAddresses, triggerType, finalFailRetryCount);
    }

    /**
     * 进行调度
     */
    private static void processTrigger(JobInfo jobInfo, String groupAddresses,
                                       TriggerTypeEnum triggerType, int failRetryCount) {
        // 调度时间
        Date triggerTime = new Date();

        // 执行器路由策略
        ExecutorRouteStrategyEnum routeStrategy = ExecutorRouteStrategyEnum
                .match(jobInfo.getExecutorRouteStrategy());

        // 1 保存日志
        JobLog jobLog = new JobLog();
        jobLog.setJobId(jobInfo.getId());
        jobLog.setGroupName(jobInfo.getGroupName());
        AdminConfig.getInstance().getJobLogMapper().insertSelective(jobLog);

        // 2 初始化触发参数
        TriggerParam triggerParam = new TriggerParam();
        // 任务ID
        triggerParam.setJobId(jobInfo.getId());
        // 任务执行相关参数
        triggerParam.setExecutorHandler(jobInfo.getExecutorHandler());
        triggerParam.setExecutorParams(jobInfo.getExecutorParam());
        triggerParam.setExecutorTimeout(jobInfo.getExecutorTimeout());
        // 任务日志ID
        triggerParam.setLogId(jobLog.getId());

        // 3 选择一个执行器
        String address = null;
        if (groupAddresses != null) {
            List<String> addressList = Arrays.asList(groupAddresses.split(","));
            ResultT<String> routeAddressResult = routeStrategy.getRouter()
                    .route(triggerParam, addressList);
            if (routeAddressResult.getCode() == ResultT.SUCCESS_CODE) {
                address = routeAddressResult.getContent();
            }
        }
        String triggerTimeStr = DateFormatUtils.format(triggerTime, DATE_TIME_MS_PATTERN);
        LOGGER.info(
                "开始调度-JobId:{},LogId:{},触发类型:{},参数:{},失败重试次数:{},调度时间:{}, 执行器地址:{}",
                jobInfo.getId(), jobLog.getId(), triggerType, jobInfo.getExecutorParam(),
                failRetryCount, triggerTimeStr, address
        );

        // 4 触发远程执行器
        ResultT<String> triggerResult;
        if (address != null) {
            triggerResult = runExecutor(triggerParam, address);
        } else {
            triggerResult = new ResultT<>(ResultT.FAIL_CODE, "未找到可用的执行器");
        }
        LOGGER.info("结束调度-{}", triggerResult);

        // 5 更新 Log
        JobLog updateJobLog = new JobLog();
        updateJobLog.setId(jobLog.getId());
        // 执行相关字段
        updateJobLog.setExecutorAddress(address);
        updateJobLog.setExecutorHandler(jobInfo.getExecutorHandler());
        updateJobLog.setExecutorParam(jobInfo.getExecutorParam());
        updateJobLog.setFailRetryCount((byte) failRetryCount);
        // 调度相关字段
        updateJobLog.setTriggerTime(triggerTime);
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
            LOGGER.error("调度请求异常.执行器:[{}],原因:{}.", address, e.getMessage());
            result = new ResultT<>(ResultT.FAIL_CODE, e.getMessage());
        }
        return result;
    }

}
