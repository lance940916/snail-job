package com.snailwu.job.admin.core.thread;

import com.snailwu.job.admin.core.config.AdminConfig;
import com.snailwu.job.admin.mapper.JobAppDynamicSqlSupport;
import com.snailwu.job.admin.mapper.JobNodeDynamicSqlSupport;
import com.snailwu.job.admin.model.JobApp;
import com.snailwu.job.admin.model.JobNode;
import com.snailwu.job.core.constants.CoreConstant;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.mybatis.dynamic.sql.render.RenderingStrategies;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.TimeUnit;

import static com.snailwu.job.admin.constant.JobConstants.DATE_TIME_PATTERN;
import static com.snailwu.job.core.enums.RegistryType.AUTO;
import static org.mybatis.dynamic.sql.SqlBuilder.*;

/**
 * 将 job_node 表中的数据整理到 job_app 表中
 *
 * @author 吴庆龙
 * @date 2020/6/4 11:23 上午
 */
public class ExecutorMonitorHelper {
    private static final Logger LOGGER = LoggerFactory.getLogger(ExecutorMonitorHelper.class);

    private static Thread thread;
    private static volatile boolean running = true;

    /**
     * 启动整理注册器线程
     */
    public static void start() {
        thread = new Thread(() -> {
            while (running) {
                try {
                    // 查询并删除死亡的机器
                    Date deadDate = DateUtils.addSeconds(new Date(), CoreConstant.DEAD_TIME * -1);
                    List<JobNode> jobNodes = AdminConfig.getInstance().getJobNodeMapper().selectMany(
                            select(JobNodeDynamicSqlSupport.jobNode.allColumns())
                                    .from(JobNodeDynamicSqlSupport.jobNode)
                                    .where(JobNodeDynamicSqlSupport.updateTime, isLessThan(deadDate))
                                    .build().render(RenderingStrategies.MYBATIS3)
                    );
                    for (JobNode node : jobNodes) {
                        AdminConfig.getInstance().getJobNodeMapper().delete(
                                deleteFrom(JobNodeDynamicSqlSupport.jobNode)
                                        .where(JobNodeDynamicSqlSupport.id, isEqualTo(node.getId()))
                                        .build().render(RenderingStrategies.MYBATIS3)
                        );
                        LOGGER.warn("节点机器：{} 已死亡，最后更新时间：{}", node.getAddress(),
                                DateFormatUtils.format(node.getUpdateTime(), DATE_TIME_PATTERN));
                    }

                    // 2. 获取所有的App
                    List<JobApp> apps = AdminConfig.getInstance().getJobAppMapper().selectMany(
                            select(JobAppDynamicSqlSupport.jobApp.allColumns())
                                    .from(JobAppDynamicSqlSupport.jobApp)
                                    .where(JobAppDynamicSqlSupport.jobApp.type, isEqualTo(AUTO.getValue()))
                                    .build().render(RenderingStrategies.MYBATIS3)
                    );

                    // 3. 获取所有的Node
                    List<JobNode> nodes = AdminConfig.getInstance().getJobNodeMapper().selectMany(
                            select(JobNodeDynamicSqlSupport.jobNode.allColumns())
                                    .from(JobNodeDynamicSqlSupport.jobNode)
                                    .build().render(RenderingStrategies.MYBATIS3)
                    );

                    // 4. 合并相同 appName 的执行器地址
                    Map<String, List<String>> appAddressMap = new HashMap<>();
                    for (JobNode node : nodes) {
                        String appName = node.getAppName();
                        String address = node.getAddress().toLowerCase();
                        List<String> addresses = appAddressMap.computeIfAbsent(appName, k -> new ArrayList<>());
                        // 保证没有重复的地址
                        if (!addresses.contains(address)) {
                            addresses.add(address);
                        }
                    }

                    // 5. 更新 jobApp 中的 address 字段
                    for (JobApp app : apps) {
                        List<String> addrList = appAddressMap.get(app.getAppName());
                        String addresses = StringUtils.join(addrList, ",");
                        if (addresses == null) {
                            addresses = "";
                        }

                        // 通过id更新地址列表
                        AdminConfig.getInstance().getJobAppMapper().update(
                                update(JobAppDynamicSqlSupport.jobApp)
                                        .set(JobAppDynamicSqlSupport.addresses).equalTo(addresses)
                                        .where(JobAppDynamicSqlSupport.id, isEqualTo(app.getId()))
                                        .build().render(RenderingStrategies.MYBATIS3)
                        );
                        LOGGER.info("应用名称：{} 地址列表：{}", app.getAppName(), addresses);
                    }
                } catch (Exception e) {
                    LOGGER.error("整理执行器地址发生异常。", e);
                }

                // 休眠
                if (running) {
                    try {
                        TimeUnit.SECONDS.sleep(CoreConstant.SORT_NODE_ADDRESS_TIME);
                    } catch (InterruptedException e) {
                        LOGGER.error("休眠异常。", e);
                    }
                }
            }
        });
        thread.setDaemon(true);
        thread.setName("executor-monitor-thread");
        thread.start();
        LOGGER.info("节点整理线程-已启动。");
    }

    /**
     * 停止注册
     */
    public static void stop() {
        running = false;
        try {
            thread.interrupt();
            thread.join();
            LOGGER.info("节点整理线程-已停止。");
        } catch (InterruptedException e) {
            LOGGER.error("停止线程 {} 异常", thread.getName(), e);
        }
    }

}
