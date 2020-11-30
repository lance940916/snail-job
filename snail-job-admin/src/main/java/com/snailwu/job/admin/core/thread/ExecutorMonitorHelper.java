package com.snailwu.job.admin.core.thread;

import com.snailwu.job.admin.core.config.AdminConfig;
import com.snailwu.job.admin.mapper.JobAppDynamicSqlSupport;
import com.snailwu.job.admin.model.JobApp;
import com.snailwu.job.admin.model.JobNode;
import com.snailwu.job.core.constants.RegistryConstant;
import com.snailwu.job.core.enums.RegistryType;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.mybatis.dynamic.sql.render.RenderingStrategies;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.TimeUnit;

import static com.snailwu.job.admin.mapper.JobAppDynamicSqlSupport.jobApp;
import static com.snailwu.job.admin.mapper.JobNodeDynamicSqlSupport.jobNode;
import static com.snailwu.job.admin.mapper.JobNodeDynamicSqlSupport.updateTime;
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
                    Date deadDate = DateUtils.addSeconds(new Date(), RegistryConstant.DEAD_TIME * -1);
                    AdminConfig.getInstance().getJobNodeMapper().delete(
                            deleteFrom(jobNode)
                                    .where(updateTime, isLessThanOrEqualTo(deadDate))
                                    .build().render(RenderingStrategies.MYBATIS3)
                    );

                    // 2. 获取所有的分组
                    List<JobApp> groupList = AdminConfig.getInstance().getJobAppMapper().selectMany(
                            select(jobApp.allColumns())
                                    .from(jobApp)
                                    .where(jobApp.type, isEqualTo(RegistryType.AUTO.getValue()))
                                    .build().render(RenderingStrategies.MYBATIS3)
                    );

                    // 3. 获取所有的执行器
                    List<JobNode> executorList = AdminConfig.getInstance().getJobNodeMapper().selectMany(
                            select(jobNode.allColumns())
                                    .from(jobNode)
                                    .build().render(RenderingStrategies.MYBATIS3)
                    );

                    // 4. 合并相同 appName 的执行器地址
                    Map<String, List<String>> appAddressMap = new HashMap<>();
                    for (JobNode executor : executorList) {
                        String groupName = executor.getAppName();
                        String address = executor.getAddress().toLowerCase();
                        List<String> addresses = appAddressMap.computeIfAbsent(groupName, k -> new ArrayList<>());
                        // 保证没有重复的地址
                        if (!addresses.contains(address)) {
                            addresses.add(address);
                        }
                    }

                    // 5. 更新 jobGroup 中的 address 字段
                    for (JobApp jobApp : groupList) {
                        List<String> addrList = appAddressMap.get(jobApp.getAppName());
                        String addresses = StringUtils.join(addrList, ",");

                        // 更新.通过id更新地址列表
                        AdminConfig.getInstance().getJobAppMapper().update(
                                update(JobAppDynamicSqlSupport.jobApp)
                                        .set(JobAppDynamicSqlSupport.addresses).equalTo(addresses)
                                        .where(JobAppDynamicSqlSupport.id, isEqualTo(jobApp.getId()))
                                        .build().render(RenderingStrategies.MYBATIS3)
                        );
                    }
                } catch (Exception e) {
                    LOGGER.error("整理执行器地址发生异常.", e);
                }

                // 休眠
                try {
                    TimeUnit.SECONDS.sleep(RegistryConstant.BEAT_TIME);
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
