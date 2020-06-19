package com.snailwu.job.admin.trigger;

import com.snailwu.job.admin.core.conf.AdminConfig;
import com.snailwu.job.admin.core.model.JobExecutor;
import com.snailwu.job.admin.core.model.JobInfo;
import com.snailwu.job.admin.core.route.ExecutorRouteStrategyEnum;
import com.snailwu.job.admin.mapper.JobExecutorDynamicSqlSupport;
import com.snailwu.job.admin.mapper.JobInfoDynamicSqlSupport;
import com.snailwu.job.core.biz.model.ResultT;
import com.snailwu.job.core.biz.model.TriggerParam;
import com.snailwu.job.core.executor.SnailJobExecutor;
import org.mybatis.dynamic.sql.SqlBuilder;
import org.mybatis.dynamic.sql.render.RenderingStrategies;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.transform.Result;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.snailwu.job.admin.core.route.ExecutorRouteStrategyEnum.SHARDING_BROADCAST;
import static org.mybatis.dynamic.sql.SqlBuilder.isEqualTo;
import static org.mybatis.dynamic.sql.SqlBuilder.select;

/**
 * @author 吴庆龙
 * @date 2020/6/17 1:56 下午
 */
public class SnailJobTrigger {
    private static final Logger log = LoggerFactory.getLogger(SnailJobTrigger.class);

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
    }

    private static ResultT<String> runTask(TriggerParam triggerParam, String address) {
        ResultT<String> result = null;
        try {
            SnailJobExecutor

        } catch (Exception e) {

        }

    }

}
