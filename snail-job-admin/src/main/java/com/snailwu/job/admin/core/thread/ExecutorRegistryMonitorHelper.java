package com.snailwu.job.admin.core.thread;

import com.snailwu.job.admin.core.conf.AdminConfig;
import com.snailwu.job.admin.core.model.JobExecutor;
import com.snailwu.job.admin.core.model.JobGroup;
import com.snailwu.job.admin.mapper.JobGroupDynamicSqlSupport;
import com.snailwu.job.core.enums.RegistryConfig;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.mybatis.dynamic.sql.delete.render.DeleteStatementProvider;
import org.mybatis.dynamic.sql.render.RenderingStrategies;
import org.mybatis.dynamic.sql.select.render.SelectStatementProvider;
import org.mybatis.dynamic.sql.update.render.UpdateStatementProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

import static com.snailwu.job.admin.mapper.JobExecutorDynamicSqlSupport.jobExecutor;
import static com.snailwu.job.admin.mapper.JobExecutorDynamicSqlSupport.updateTime;
import static com.snailwu.job.admin.mapper.JobGroupDynamicSqlSupport.jobGroup;
import static org.mybatis.dynamic.sql.SqlBuilder.*;

/**
 * 将 job_executor_node 表中的数据整理到 job_executor 表中
 *
 * @author 吴庆龙
 * @date 2020/6/4 11:23 上午
 */
public class ExecutorRegistryMonitorHelper {
    public static final Logger log = LoggerFactory.getLogger(ExecutorRegistryMonitorHelper.class);

    private static final ExecutorRegistryMonitorHelper instance = new ExecutorRegistryMonitorHelper();

    public static ExecutorRegistryMonitorHelper getInstance() {
        return instance;
    }

    private Thread registryThread;
    private volatile boolean toStop = false;

    /**
     * 开始注册
     */
    public void start() {
        registryThread = new Thread(() -> {
            while (!toStop) {
                // 删除所有不活跃的执行器节点
                Date deadDate = DateUtils.addSeconds(new Date(), RegistryConfig.BEAT_TIMEOUT * -1);
                DeleteStatementProvider deleteDeadWorkerStat = deleteFrom(jobExecutor)
                        .where(updateTime, isLessThanOrEqualTo(deadDate))
                        .build().render(RenderingStrategies.MYBATIS3);
                AdminConfig.getInstance().getJobExecutorMapper().delete(deleteDeadWorkerStat);

                // 列出所有的分组
                SelectStatementProvider jobExecutorSelect = select(jobGroup.allColumns())
                        .from(jobGroup)
                        .orderBy(JobGroupDynamicSqlSupport.name, JobGroupDynamicSqlSupport.uuid, JobGroupDynamicSqlSupport.id)
                        .build().render(RenderingStrategies.MYBATIS3);
                List<JobGroup> groupList = AdminConfig.getInstance().getJobGroupMapper().selectMany(jobExecutorSelect);

                // 将活跃的执行器节点整理到执行器表中
                SelectStatementProvider onlineNodeSelect = select(jobExecutor.allColumns())
                        .from(jobExecutor)
                        .where(updateTime, isGreaterThan(deadDate))
                        .build().render(RenderingStrategies.MYBATIS3);
                List<JobExecutor> executorList = AdminConfig.getInstance().getJobExecutorMapper().selectMany(onlineNodeSelect);
                Map<String, List<String>> appNameAddressMap = new HashMap<>();
                for (JobExecutor executor : executorList) {
                    String groupUuid = executor.getGroupUuid();
                    String address = executor.getAddress();

                    List<String> addressList = appNameAddressMap.get(groupUuid);
                    if (addressList == null) {
                        addressList = new ArrayList<>();
                        addressList.add(address);
                        appNameAddressMap.put(groupUuid, addressList);
                    }
                    // 没有包含，则添加进去
                    if (!addressList.contains(address)) {
                        addressList.add(address);
                    }
                }

                // 更新 jobExecutor 中的 address 列表
                for (JobGroup group : groupList) {
                    List<String> addrList = appNameAddressMap.get(group.getUuid());
                    String addressListStr = StringUtils.join(addrList, ",");
                    UpdateStatementProvider usp = update(jobGroup)
                            .set(JobGroupDynamicSqlSupport.addressList).equalTo(addressListStr)
                            .where(JobGroupDynamicSqlSupport.id, isEqualTo(group.getId()))
                            .build().render(RenderingStrategies.MYBATIS3);
                    AdminConfig.getInstance().getJobGroupMapper().update(usp);
                }
            }
        });
        registryThread.setDaemon(true);
        registryThread.setName("JobNodeRegistryMonitorThread");
        registryThread.start();
    }

    /**
     * 停止注册
     */
    public void stop() {
        toStop = true;
        registryThread.interrupt();
        try {
            registryThread.join();
        } catch (InterruptedException e) {
            log.error(e.getMessage(), e);
        }
    }

}
