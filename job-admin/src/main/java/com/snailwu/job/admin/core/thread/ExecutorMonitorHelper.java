package com.snailwu.job.admin.core.thread;

import com.snailwu.job.admin.core.conf.AdminConfig;
import com.snailwu.job.admin.core.model.JobExecutor;
import com.snailwu.job.admin.core.model.JobGroup;
import com.snailwu.job.admin.mapper.JobGroupDynamicSqlSupport;
import com.snailwu.job.core.enums.RegistryConfig;
import com.snailwu.job.core.enums.RegistryType;
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
public class ExecutorMonitorHelper {
    private static final Logger LOGGER = LoggerFactory.getLogger(ExecutorMonitorHelper.class);

    private static Thread monitorThread;
    private static volatile boolean stopFlag = false;

    /**
     * 启动整理注册器线程
     */
    public static void start() {
        monitorThread = new Thread(() -> {
            while (!stopFlag) {
                // 是要用 try-catch 全部代码，保证异常情况不会退出 while 循环
                try {
                    // 1. 删除所有不活跃的执行器
                    Date deadDate = DateUtils.addSeconds(new Date(), RegistryConfig.DEAD_TIME * -1);
                    AdminConfig.getInstance().getJobExecutorMapper().delete(
                            deleteFrom(jobExecutor)
                                    .where(updateTime, isLessThanOrEqualTo(deadDate))
                                    .build().render(RenderingStrategies.MYBATIS3)
                    );

                    // 2. 获取所有的分组
                    List<JobGroup> groupList = AdminConfig.getInstance().getJobGroupMapper().selectMany(
                            select(jobGroup.allColumns())
                                    .from(jobGroup)
                                    .where(jobGroup.type, isEqualTo(RegistryType.AUTO.getValue()))
                                    .build().render(RenderingStrategies.MYBATIS3)
                    );

                    // 3. 获取所有的执行器
                    List<JobExecutor> executorList = AdminConfig.getInstance().getJobExecutorMapper().selectMany(
                            select(jobExecutor.allColumns())
                                    .from(jobExecutor)
                                    .build().render(RenderingStrategies.MYBATIS3)
                    );

                    // 4. 合并相同 groupName 的执行器地址
                    Map<String, List<String>> groupAddressMap = new HashMap<>();
                    for (JobExecutor executor : executorList) {
                        String groupName = executor.getGroupName();
                        String address = executor.getAddress().toLowerCase();
                        List<String> addressList = groupAddressMap.computeIfAbsent(groupName, k -> new ArrayList<>());
                        // 保证没有重复的地址
                        if (!addressList.contains(address)) {
                            addressList.add(address);
                        }
                    }

                    // 5. 更新 jobGroup 中的 address 字段
                    for (JobGroup group : groupList) {
                        List<String> addrList = groupAddressMap.get(group.getName());
                        String addresses = StringUtils.join(addrList, ",");

                        // 更新.通过id更新地址列表
                        AdminConfig.getInstance().getJobGroupMapper().update(
                                update(jobGroup)
                                        .set(JobGroupDynamicSqlSupport.addressList).equalTo(addresses)
                                        .where(JobGroupDynamicSqlSupport.id, isEqualTo(group.getId()))
                                        .build().render(RenderingStrategies.MYBATIS3)
                        );
                    }
                } catch (Exception e) {
                    LOGGER.error("整理执行器地址发生异常.", e);
                }

                // 休眠
                try {
                    TimeUnit.SECONDS.sleep(RegistryConfig.BEAT_TIME);
                } catch (InterruptedException e) {
                    if (!stopFlag) {
                        LOGGER.error("休眠异常.", e);
                    }
                }
            }
        });
        monitorThread.setDaemon(true);
        monitorThread.setName("executor-monitor-thread");
        monitorThread.start();
    }

    /**
     * 停止注册
     */
    public static void stop() {
        stopFlag = true;
        monitorThread.interrupt();
        try {
            monitorThread.join();
        } catch (InterruptedException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

}
