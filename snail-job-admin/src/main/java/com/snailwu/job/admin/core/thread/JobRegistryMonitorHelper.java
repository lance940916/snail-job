package com.snailwu.job.admin.core.thread;

import com.snailwu.job.admin.core.conf.AdminConfig;
import com.snailwu.job.admin.core.model.JobExecutor;
import com.snailwu.job.admin.core.model.JobExecutorNode;
import com.snailwu.job.admin.mapper.JobExecutorNodeDynamicSqlSupport;
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

import static com.snailwu.job.admin.mapper.JobExecutorDynamicSqlSupport.*;
import static com.snailwu.job.admin.mapper.JobExecutorNodeDynamicSqlSupport.jobExecutorNode;
import static com.snailwu.job.admin.mapper.JobExecutorNodeDynamicSqlSupport.updateTime;
import static org.mybatis.dynamic.sql.SqlBuilder.*;

/**
 * 将 job_executor_node 表中的数据整理到 job_executor 表中
 *
 * @author 吴庆龙
 * @date 2020/6/4 11:23 上午
 */
public class JobRegistryMonitorHelper {
    public static final Logger log = LoggerFactory.getLogger(JobRegistryMonitorHelper.class);

    private static final JobRegistryMonitorHelper instance = new JobRegistryMonitorHelper();

    public static JobRegistryMonitorHelper getInstance() {
        return instance;
    }

    private Thread registryThread;
    private volatile boolean toStop = false;

    public void start() {
        registryThread = new Thread(() -> {
            while (!toStop) {
                // 在执行节点注册表中删除不活跃的机器
                Date deadDate = DateUtils.addSeconds(new Date(), RegistryConfig.BEAT_TIMEOUT * -1);
                DeleteStatementProvider deleteDeadWorkerStat = deleteFrom(jobExecutorNode)
                        .where(updateTime, isLessThanOrEqualTo(deadDate))
                        .build().render(RenderingStrategies.MYBATIS3);
                AdminConfig.getInstance().getJobExecutorNodeMapper().delete(deleteDeadWorkerStat);

                // 列出所有的执行器
                SelectStatementProvider jobExecutorSelect = select(jobExecutor.allColumns())
                        .from(jobExecutor)
                        .orderBy(JobExecutorNodeDynamicSqlSupport.appName, title, id)
                        .build().render(RenderingStrategies.MYBATIS3);
                List<JobExecutor> executorList = AdminConfig.getInstance().getJobExecutorMapper().selectMany(jobExecutorSelect);

                // 将活跃的执行器节点整理到执行器表中
                SelectStatementProvider onlineNodeSelect = select(jobExecutorNode.allColumns())
                        .from(jobExecutorNode)
                        .where(updateTime, isGreaterThan(deadDate))
                        .build().render(RenderingStrategies.MYBATIS3);
                List<JobExecutorNode> nodeList = AdminConfig.getInstance().getJobExecutorNodeMapper().selectMany(onlineNodeSelect);
                Map<String, List<String>> appNameAddressMap = new HashMap<>();
                for (JobExecutorNode node : nodeList) {
                    String appName = node.getAppName();
                    String address = node.getAddress();

                    List<String> addressList = appNameAddressMap.get(appName);
                    if (addressList == null) {
                        addressList = new ArrayList<>();
                        addressList.add(address);
                        appNameAddressMap.put(appName, addressList);
                    }
                    // 没有包含，则添加进去
                    if (!addressList.contains(address)) {
                        addressList.add(address);
                    }
                }

                // 更新 jobExecutor 中的 address 列表
                for (JobExecutor executor : executorList) {
                    List<String> addrList = appNameAddressMap.get(executor.getAppName());
                    String addressListStr = StringUtils.join(addrList, ",");
                    UpdateStatementProvider statementProvider = update(jobExecutor)
                            .set(addressList).equalTo(addressListStr)
                            .where(id, isEqualTo(executor.getId()))
                            .build().render(RenderingStrategies.MYBATIS3);
                    AdminConfig.getInstance().getJobExecutorMapper().update(statementProvider);
                }
            }
        });
        registryThread.setDaemon(true);
        registryThread.setName("JobNodeRegistryThread");
        registryThread.start();
    }

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
