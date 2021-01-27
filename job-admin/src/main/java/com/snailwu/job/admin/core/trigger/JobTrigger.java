package com.snailwu.job.admin.core.trigger;

import com.snailwu.job.admin.core.config.AdminConfig;
import com.snailwu.job.admin.core.route.ExecRouteStrategyEnum;
import com.snailwu.job.admin.core.scheduler.JobScheduler;
import com.snailwu.job.admin.mapper.JobAppDynamicSqlSupport;
import com.snailwu.job.admin.mapper.JobInfoDynamicSqlSupport;
import com.snailwu.job.admin.model.JobApp;
import com.snailwu.job.admin.model.JobInfo;
import com.snailwu.job.admin.model.JobLog;
import com.snailwu.job.core.biz.ExecutorBiz;
import com.snailwu.job.core.biz.model.ResultT;
import com.snailwu.job.core.biz.model.TriggerParam;
import com.snailwu.job.core.enums.AlarmStatus;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.mybatis.dynamic.sql.render.RenderingStrategies;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

import static com.snailwu.job.admin.constant.JobConstants.DATE_TIME_MS_PATTERN;
import static com.snailwu.job.admin.core.route.ExecRouter.NO_FOUND_ADDRESS_MSG;
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

    /**
     * 触发 Job
     *
     * @param execParam 执行参数，手动执行时页面填写的会覆盖任务中的参数
     */
    public static void trigger(int jobId, TriggerTypeEnum triggerType, int failRetryCount, String execParam) {
        // 查询任务信息
        JobInfo jobInfo = AdminConfig.getInstance().getJobInfoMapper().selectOne(
                select(JobInfoDynamicSqlSupport.jobInfo.allColumns())
                        .from(JobInfoDynamicSqlSupport.jobInfo)
                        .where(JobInfoDynamicSqlSupport.id, isEqualTo(jobId))
                        .build().render(RenderingStrategies.MYBATIS3)
        ).orElse(null);
        if (jobInfo == null) {
            LOGGER.error("无效的任务。ID：{}", jobId);
            return;
        }

        // 查询任务分组信息
        String appName = jobInfo.getAppName();
        JobApp jobGroup = AdminConfig.getInstance().getJobAppMapper().selectOne(
                select(JobAppDynamicSqlSupport.id, JobAppDynamicSqlSupport.addresses)
                        .from(JobAppDynamicSqlSupport.jobApp)
                        .where(JobAppDynamicSqlSupport.appName, isEqualTo(appName))
                        .build().render(RenderingStrategies.MYBATIS3)
        ).orElse(null);
        if (jobGroup == null) {
            LOGGER.error("任务应用信息不存在。name：{}", appName);
            return;
        }

        // 节点地址集合
        String addresses = jobGroup.getAddresses();

        // 优先使用传入的调度参数
        if (execParam != null) {
            jobInfo.setExecParam(execParam);
        }

        // 优先使用参数的值
        int finalFailRetryCount = failRetryCount >= 0 ? failRetryCount : jobInfo.getExecFailRetryCount();

        // 进行调度
        doTrigger(jobInfo, addresses, triggerType, finalFailRetryCount);
    }

    /**
     * 进行调度
     */
    private static void doTrigger(JobInfo jobInfo, String appAddresses, TriggerTypeEnum triggerType,
                                  int failRetryCount) {
        // 调度时间
        Date triggerTime = new Date();

        // 执行器路由策略，不可能为null
        ExecRouteStrategyEnum routeStrategy = ExecRouteStrategyEnum.match(jobInfo.getExecRouteStrategy());

        // 保存日志
        JobLog jobLog = new JobLog();
        jobLog.setJobId(jobInfo.getId());
        jobLog.setAppName(jobInfo.getAppName());
        AdminConfig.getInstance().getJobLogMapper().insertSelective(jobLog);

        // 2 初始化触发参数
        TriggerParam triggerParam = new TriggerParam();
        triggerParam.setJobId(jobInfo.getId());
        triggerParam.setExecHandler(jobInfo.getExecHandler());
        triggerParam.setExecParam(jobInfo.getExecParam());
        triggerParam.setExecTimeout(jobInfo.getExecTimeout());
        triggerParam.setLogId(jobLog.getId());

        // 3 选择一个节点执行
        String nodeAddress = null;
        if (appAddresses != null) {
            String[] appAddressArray = StringUtils.split(appAddresses, ",");
            ResultT<String> routeAddressResult = routeStrategy.getRouter().route(triggerParam, appAddressArray);
            if (routeAddressResult.getCode() == ResultT.SUCCESS_CODE) {
                nodeAddress = routeAddressResult.getContent();
            }
        }
        String triggerTimeStr = DateFormatUtils.format(triggerTime, DATE_TIME_MS_PATTERN);
        LOGGER.info("执行调度-JobId:{},LogId:{},触发类型:{},参数:{},失败重试次数:{},调度时间:{},节点地址:{}",
                jobInfo.getId(), jobLog.getId(), triggerType, jobInfo.getExecParam(),
                failRetryCount, triggerTimeStr, nodeAddress
        );

        // 4 触发远程执行器
        ResultT<String> triggerResult;
        if (nodeAddress != null) {
            triggerResult = process(triggerParam, nodeAddress);
        } else {
            triggerResult = new ResultT<>(ResultT.FAIL_CODE, NO_FOUND_ADDRESS_MSG);
        }
        LOGGER.info("完成调度-{}", triggerResult);

        // 5 更新 Log
        JobLog updateJobLog = new JobLog();
        updateJobLog.setId(jobLog.getId());
        // 执行参数
        updateJobLog.setExecAddress(nodeAddress);
        updateJobLog.setExecHandler(jobInfo.getExecHandler());
        updateJobLog.setExecParam(jobInfo.getExecParam());
        updateJobLog.setFailRetryCount((byte) failRetryCount);

        // 无 execTime、execCode和execMsg

        // 调度信息
        updateJobLog.setTriggerTime(triggerTime);
        updateJobLog.setTriggerCode(triggerResult.getCode());
        updateJobLog.setTriggerMsg(triggerResult.getMsg());
        // 告警状态
        updateJobLog.setAlarmStatus(AlarmStatus.DEFAULT.getValue());
        AdminConfig.getInstance().getJobLogMapper().updateByPrimaryKeySelective(updateJobLog);
    }

    /**
     * 执行任务
     */
    private static ResultT<String> process(TriggerParam triggerParam, String address) {
        ResultT<String> result;
        try {
            ExecutorBiz executorBiz = JobScheduler.obtainOrCreateExecutorBiz(address);
            result = executorBiz.run(triggerParam);
        } catch (Exception e) {
            LOGGER.error("调度请求异常。", e);
            result = new ResultT<>(ResultT.FAIL_CODE, e.getMessage());
        }
        return result;
    }

}
