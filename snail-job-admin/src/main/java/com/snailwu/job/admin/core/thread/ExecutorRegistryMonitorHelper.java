package com.snailwu.job.admin.core.thread;

import com.snailwu.job.admin.core.conf.AdminConfig;
import com.snailwu.job.admin.core.model.JobExecutor;
import com.snailwu.job.admin.core.model.JobGroup;
import com.snailwu.job.admin.mapper.JobGroupDynamicSqlSupport;
import com.snailwu.job.core.enums.RegistryConfig;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.mybatis.dynamic.sql.render.RenderingStrategies;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.TimeUnit;

import static com.snailwu.job.admin.mapper.JobExecutorDynamicSqlSupport.jobExecutor;
import static com.snailwu.job.admin.mapper.JobExecutorDynamicSqlSupport.updateTime;
import static com.snailwu.job.admin.mapper.JobGroupDynamicSqlSupport.jobGroup;
import static org.mybatis.dynamic.sql.SqlBuilder.*;

/**
 * 将 job_executor 表中的数据整理到 job_group 表中
 *
 * @author 吴庆龙
 * @date 2020/6/4 11:23 上午
 */
public class ExecutorRegistryMonitorHelper {
    public static final Logger log = LoggerFactory.getLogger(ExecutorRegistryMonitorHelper.class);

    private static Thread registryThread;
    private static volatile boolean toStop = false;

    /**
     * 启动整理注册器线程
     */
    public static void start() {
        registryThread = new Thread(() -> {
            while (!toStop) {
                // TODO 加入分布式锁，保证多个调度中心同时只有一个可以进行整理

                // 删除所有不活跃的执行器
                Date deadDate = DateUtils.addSeconds(new Date(), RegistryConfig.DEAD_TIMEOUT * -1);
                AdminConfig.getInstance().getJobExecutorMapper().delete(
                        deleteFrom(jobExecutor).where(updateTime, isLessThanOrEqualTo(deadDate))
                                .build().render(RenderingStrategies.MYBATIS3)
                );

                // 获取所有的分组
                List<JobGroup> groupList = AdminConfig.getInstance().getJobGroupMapper().selectMany(
                        select(jobGroup.allColumns()).from(jobGroup)
                                .build().render(RenderingStrategies.MYBATIS3)
                );

                // 获取所有的执行器
                List<JobExecutor> executorList = AdminConfig.getInstance().getJobExecutorMapper().selectMany(
                        select(jobExecutor.allColumns()).from(jobExecutor)
                                .build().render(RenderingStrategies.MYBATIS3)
                );

                // 合并同一个分组的执行器地址
                Map<String, List<String>> groupAddressMap = new HashMap<>();
                for (JobExecutor executor : executorList) {
                    String groupName = executor.getGroupName();
                    String address = executor.getAddress();

                    List<String> addressList = groupAddressMap.get(groupName);
                    if (addressList == null) {
                        // 添加新的地址
                        addressList = new ArrayList<>();
                        addressList.add(address);
                        groupAddressMap.put(groupName, addressList);
                    } else if (!addressList.contains(address)) {
                        // 保证没有重复的地址
                        addressList.add(address);
                    }
                }

                // 更新 jobGroup 中的 address 列表
                for (JobGroup group : groupList) {
                    // 执行器地址
                    List<String> addrList = groupAddressMap.get(group.getName());
                    String addresses = StringUtils.join(addrList, ",");
                    if (addresses == null) {
                        continue;
                    }

                    // 更新
                    AdminConfig.getInstance().getJobGroupMapper().update(
                            update(jobGroup).set(JobGroupDynamicSqlSupport.addressList).equalTo(addresses)
                                    .where(JobGroupDynamicSqlSupport.id, isEqualTo(group.getId()))
                                    .build().render(RenderingStrategies.MYBATIS3)
                    );
                }

                // 休眠
                try {
                    TimeUnit.SECONDS.sleep(RegistryConfig.BEAT_TIMEOUT);
                } catch (InterruptedException e) {
                    if (!toStop) {
                        log.error("执行器注册监听线程休眠异常", e);
                    }
                }
            }
        });
        registryThread.setDaemon(true);
        registryThread.setName("ExecutorRegistryMonitorThread");
        registryThread.start();
    }

    /**
     * 停止注册
     */
    public static void stop() {
        toStop = true;
        registryThread.interrupt();
        try {
            registryThread.join();
        } catch (InterruptedException e) {
            log.error(e.getMessage(), e);
        }
    }

}
