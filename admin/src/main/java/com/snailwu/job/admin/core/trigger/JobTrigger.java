package com.snailwu.job.admin.core.trigger;

import com.snailwu.job.admin.core.config.AdminConfig;
import com.snailwu.job.admin.core.route.ExecRouteStrategyEnum;
import com.snailwu.job.admin.core.scheduler.JobScheduler;
import com.snailwu.job.admin.model.JobApp;
import com.snailwu.job.admin.model.JobInfo;
import com.snailwu.job.admin.model.JobLog;
import com.snailwu.job.core.biz.ExecutorBiz;
import com.snailwu.job.core.biz.model.ResultT;
import com.snailwu.job.core.biz.model.TriggerParam;
import com.snailwu.job.core.enums.AlarmStatus;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

import static com.snailwu.job.admin.constant.AdminConstants.DATE_TIME_PATTERN;
import static com.snailwu.job.admin.core.route.ExecRouter.NO_FOUND_ADDRESS_MSG;

/**
 * 进行任务的调度
 *
 * @author 吴庆龙
 * @date 2020/6/17 1:56 下午
 */
public class JobTrigger {
    private static final Logger logger = LoggerFactory.getLogger(JobTrigger.class);

    /**
     * 触发 Job
     *
     * @param jobId                  任务的id
     * @param triggerType            触发类型
     * @param overrideFailRetryCount 如果指定则使用该值，-1表示不指定
     * @param overrideExecParam      如果指定则使用改制，null表示不指定
     */
    public static void trigger(int jobId, TriggerTypeEnum triggerType, Integer overrideFailRetryCount,
                               String overrideExecParam) {
        // 查询任务信息
        JobInfo jobInfo = AdminConfig.getInstance().getJobInfoMapper().selectByPrimaryKey(jobId);
        if (jobInfo == null) {
            logger.error("无效的任务。jobId：{}", jobId);
            return;
        }

        // 查询应用信息
        String appName = jobInfo.getAppName();
        JobApp jobApp = AdminConfig.getInstance().getJobAppMapper().selectByAppName(appName);
        if (jobApp == null) {
            logger.error("无效的应用。appName：{}", appName);
            return;
        }

        // 优先使用传入的调度参数
        if (overrideExecParam != null) {
            jobInfo.setExecParam(overrideExecParam);
        }

        // 优先使用参数的值
        if (overrideFailRetryCount != null && overrideFailRetryCount >= 0) {
            jobInfo.setExecFailRetryCount(overrideFailRetryCount.byteValue());
        }

        // 进行调度
        doTrigger(jobInfo, jobApp.getAddresses(), triggerType);
    }

    /**
     * 进行调度
     */
    private static void doTrigger(JobInfo info, String addresses, TriggerTypeEnum triggerType) {
        // 调度时间
        Date triggerTime = new Date();

        // 执行器路由策略，不可能为null
        ExecRouteStrategyEnum routeStrategy = ExecRouteStrategyEnum.match(info.getExecRouteStrategy());

        // 保存日志
        JobLog log = new JobLog();
        log.setJobId(info.getId());
        log.setAppName(info.getAppName());
        AdminConfig.getInstance().getJobLogMapper().insert(log);

        // 初始化触发参数
        TriggerParam triggerParam = new TriggerParam();
        triggerParam.setJobId(info.getId());
        triggerParam.setExecHandler(info.getExecHandler());
        triggerParam.setExecParam(info.getExecParam());
        triggerParam.setExecTimeout(info.getExecTimeout());
        triggerParam.setLogId(log.getId());

        // 选择一个执行器执行
        String executorAddress = null;
        if (addresses != null && addresses.length() > 0) {
            String[] addressArray = StringUtils.split(addresses, ",");
            ResultT<String> routeAddress = routeStrategy.getRouter().route(triggerParam, addressArray);
            if (routeAddress.getCode() == ResultT.SUCCESS_CODE) {
                executorAddress = routeAddress.getContent();
            }
        }

        // 日志
        String triggerTimeStr = DateFormatUtils.format(triggerTime, DATE_TIME_PATTERN);
        logger.info("执行调度-JobId:{},LogId:{},触发类型:{},参数:{},失败重试次数:{},调度时间:{},节点地址:{}",
                info.getId(), log.getId(), triggerType, info.getExecParam(),
                info.getExecFailRetryCount(), triggerTimeStr, executorAddress
        );

        // 触发远程执行器
        ResultT<String> triggerResult;
        if (executorAddress != null) {
            triggerResult = process(triggerParam, executorAddress);
        } else {
            triggerResult = new ResultT<>(ResultT.FAIL_CODE, NO_FOUND_ADDRESS_MSG);
        }
        logger.info("调度完成。结果：{}", triggerResult);

        // 更新 Log
        JobLog updateLog = new JobLog();
        updateLog.setId(log.getId());
        // 执行参数
        updateLog.setExecAddress(executorAddress);
        updateLog.setExecHandler(info.getExecHandler());
        updateLog.setExecParam(info.getExecParam());
        updateLog.setFailRetryCount(info.getExecFailRetryCount());
        // 无 execTime、execCode和execMsg
        // 调度信息
        updateLog.setTriggerTime(triggerTime);
        updateLog.setTriggerCode(triggerResult.getCode());
        updateLog.setTriggerMsg(triggerResult.getMsg());
        // 告警状态
        updateLog.setAlarmStatus(AlarmStatus.DEFAULT.getValue());
        AdminConfig.getInstance().getJobLogMapper().updateTriggerResultById(updateLog);
    }

    /**
     * 执行任务
     */
    private static ResultT<String> process(TriggerParam triggerParam, String address) {
        ResultT<String> result;
        try {
            ExecutorBiz executorBiz = JobScheduler.getOrCreateExecutorBiz(address);
            result = executorBiz.run(triggerParam);
        } catch (Exception e) {
            logger.error("调度请求异常。", e);
            result = new ResultT<>(ResultT.FAIL_CODE, e.getMessage());
        }
        return result;
    }

}
