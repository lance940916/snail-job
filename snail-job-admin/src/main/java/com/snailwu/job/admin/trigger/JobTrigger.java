package com.snailwu.job.admin.trigger;

import com.snailwu.job.admin.core.conf.AdminConfig;
import com.snailwu.job.admin.core.route.ExecutorRouteStrategyEnum;
import com.snailwu.job.admin.core.scheduler.SnailJobScheduler;
import com.snailwu.job.admin.utils.I18nUtil;
import com.snailwu.job.core.biz.ExecutorBiz;
import com.snailwu.job.core.biz.model.ResultT;
import com.snailwu.job.core.biz.model.TriggerParam;
import com.snailwu.job.core.enums.ExecutorBlockStrategyEnum;
import org.mybatis.dynamic.sql.SqlBuilder;
import org.mybatis.dynamic.sql.render.RenderingStrategies;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static com.snailwu.job.admin.core.route.ExecutorRouteStrategyEnum.SHARDING_BROADCAST;

/**
 * @author 吴庆龙
 * @date 2020/6/17 1:56 下午
 */
public class JobTrigger {
    private static final Logger log = LoggerFactory.getLogger(JobTrigger.class);

    /**
     * 触发 Job
     */
    public static void trigger(int jobId, TriggerTypeEnum triggerType, int failRetryCount, String executorParam, String addresses) {
        // 查询 Job 信息
        Optional<JobInfo> jobInfoOptional = AdminConfig.getInstance().getJobInfoMapper().selectOne(
                select(JobInfoDynamicSqlSupport.jobInfo.allColumns())
                        .from(JobInfoDynamicSqlSupport.jobInfo)
                        .where(JobInfoDynamicSqlSupport.id, SqlBuilder.isEqualTo(jobId))
                        .build().render(RenderingStrategies.MYBATIS3)
        );
        if (!jobInfoOptional.isPresent()) {
            log.warn("JobId:{} 无此数据", jobId);
            return;
        }

        JobInfo jobInfo = jobInfoOptional.get();
        if (executorParam != null) {
            jobInfo.setExecutorParam(executorParam);
        }

        int finalFailRetryCount = failRetryCount > 0 ? failRetryCount : jobInfo.getExecutorFailRetryCount();
        Optional<JobExecutor> jobExecutorOptional = AdminConfig.getInstance().getJobExecutorMapper().selectOne(
                select(JobExecutorDynamicSqlSupport.jobExecutor.allColumns())
                        .from(JobExecutorDynamicSqlSupport.jobExecutor)
                        .where(JobExecutorDynamicSqlSupport.id, isEqualTo(jobInfo.getExecutorId()))
                        .build().render(RenderingStrategies.MYBATIS3)
        );
        if (!jobExecutorOptional.isPresent()) {
            log.warn("执行器不存在. {}", jobInfo.getExecutorId());
            return;
        }
        JobExecutor jobExecutor = jobExecutorOptional.get();
        if (addresses != null && addresses.trim().length() > 0) {
            jobExecutor.setAddressList(addresses.trim());
        }

        String executorAddressList = jobExecutor.getAddressList();
        if (SHARDING_BROADCAST == ExecutorRouteStrategyEnum.match(jobInfo.getExecutorBlockStrategy(), null)
                && executorAddressList != null && executorAddressList.length() > 0) {
            List<String> addressList = Arrays.asList(executorAddressList.split(","));
            int addressSize = addressList.size();
            for (int i = 0; i < addressSize; i++) {
                processTrigger(jobExecutor, jobInfo, finalFailRetryCount, triggerType, i, addressSize);
            }
        } else {
            processTrigger(jobExecutor, jobInfo, finalFailRetryCount, triggerType, 0, 1);
        }
    }

    private static void processTrigger(JobExecutor executor, JobInfo jobInfo, int finalFailRetryCount,
                                       TriggerTypeEnum triggerType, int index, int total) {
        ExecutorBlockStrategyEnum blockStrategy = ExecutorBlockStrategyEnum.match(jobInfo.getExecutorBlockStrategy(),
                ExecutorBlockStrategyEnum.SERIAL_EXECUTION);
        ExecutorRouteStrategyEnum routeStrategy = ExecutorRouteStrategyEnum.match(jobInfo.getExecutorRouteStrategy(),
                ExecutorRouteStrategyEnum.RANDOM);
        String shardingParam = (ExecutorRouteStrategyEnum.SHARDING_BROADCAST == routeStrategy) ?
                String.valueOf(index).concat("/").concat(String.valueOf(total)) : null;

        // 1 保存日志
        JobLog jobLog = new JobLog();
        jobLog.setExecutorId(jobInfo.getExecutorId());
        jobLog.setJobId(jobInfo.getId());
        jobLog.setTriggerTime(new Date());
        AdminConfig.getInstance().getJobLogMapper().insertSelective(jobLog);
        log.info("任务触发开始。jobId:{}", jobLog.getJobId());

        // 2 初始化触发参数
        TriggerParam triggerParam = new TriggerParam();
        triggerParam.setJobId(jobInfo.getId());
        triggerParam.setExecutorHandler(jobInfo.getExecutorHandler());
        triggerParam.setExecutorParams(jobInfo.getExecutorParam());
        triggerParam.setExecutorBlockStrategy(jobInfo.getExecutorBlockStrategy());
        triggerParam.setExecutorTimeout(jobInfo.getExecutorTimeout());
        triggerParam.setLogId(jobLog.getId());
        triggerParam.setLogDateTime(jobLog.getTriggerTime().getTime());
        triggerParam.setBroadcastIndex(index);
        triggerParam.setBroadcastTotal(total);

        // 3 初始化地址
        String address = null;
        ResultT<String> routeAddressResult = null;
        if (executor.getAddressList() != null && !executor.getAddressList().isEmpty()) {
            List<String> addressList = Arrays.asList(executor.getAddressList().split(","));
            int addressSize = addressList.size();
            if (SHARDING_BROADCAST == routeStrategy) {
                if (index < addressSize) {
                    address = addressList.get(index);
                } else {
                    address = addressList.get(0);
                }
            } else {
                routeAddressResult = routeStrategy.getRouter().route(triggerParam, addressList);
                if (routeAddressResult.getCode() == ResultT.SUCCESS_CODE) {
                    address = routeAddressResult.getContent();
                }
            }
        } else {
            routeAddressResult = new ResultT<>(ResultT.FAIL_CODE, I18nUtil.getString("jobconf_trigger_address_empty"));
        }

        // 4 触发远程执行器
        ResultT<String> triggerResult = null;
        if (address != null) {
            triggerResult = runExecutor(triggerParam, address);
        } else {
            triggerResult = new ResultT<>(ResultT.FAIL_CODE, null);
        }

        // 更新 Log
        JobLog updateJobLog = new JobLog();
        updateJobLog.setId(jobLog.getId());
        updateJobLog.setExecutorNodeAddress(address);
        updateJobLog.setExecutorHandler(jobInfo.getExecutorHandler());
        updateJobLog.setExecutorParam(jobInfo.getExecutorParam());
        updateJobLog.setExecutorShardingParam(shardingParam);
        updateJobLog.setExecutorFailRetryCount((byte) finalFailRetryCount);
        updateJobLog.setTriggerCode(triggerResult.getCode());
        AdminConfig.getInstance().getJobLogMapper().updateByPrimaryKeySelective(updateJobLog);
    }

    /**
     * 使用执行器执行任务
     */
    private static ResultT<String> runExecutor(TriggerParam triggerParam, String address) {
        ResultT<String> result = null;
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
