package com.snailwu.job.admin.service.impl;

import com.snailwu.job.admin.core.model.JobExecutor;
import com.snailwu.job.admin.mapper.JobExecutorMapper;
import com.snailwu.job.core.biz.AdminBiz;
import com.snailwu.job.core.biz.model.CallbackParam;
import com.snailwu.job.core.biz.model.RegistryParam;
import com.snailwu.job.core.biz.model.ResultT;
import com.snailwu.job.core.enums.RegistryType;
import org.mybatis.dynamic.sql.render.RenderingStrategies;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

import static com.snailwu.job.admin.mapper.JobExecutorDynamicSqlSupport.*;
import static org.mybatis.dynamic.sql.SqlBuilder.*;

/**
 * @author 吴庆龙
 * @date 2020/6/3 11:34 上午
 */
@Service
public class AdminBizImpl implements AdminBiz {
    private static final Logger log = LoggerFactory.getLogger(AdminBizImpl.class);

    @Resource
    private JobExecutorMapper jobExecutorMapper;

    /**
     * 接收任务执行结果
     * TODO 保存到数据库中
     */
    @Override
    public ResultT<String> callback(List<CallbackParam> callbackParamList) {
        for (CallbackParam callbackParam : callbackParamList) {
            // TODO 将执行结果与任务执行日志关联起来
            log.info("回调成功. 任务执行结果: {}", callbackParam.getExecuteResult().getCode());
        }
        return ResultT.SUCCESS;
    }

    @Override
    public ResultT<String> registry(RegistryParam registryParam) {
        // 更新数据库
        int update = jobExecutorMapper.update(
                update(jobExecutor)
                        .set(updateTime).equalTo(new Date())
                        .where(groupUuid, isEqualTo(registryParam.getGroupUuid()))
                        .and(address, isEqualTo(registryParam.getExecutorAddress()))
                        .build().render(RenderingStrategies.MYBATIS3)
        );

        // 数据不存在，插入数据
        if (update < 1) {
            JobExecutor executor = new JobExecutor();
            executor.setGroupUuid(registryParam.getGroupUuid());
            executor.setAddress(registryParam.getExecutorAddress());
            executor.setRegistryType(RegistryType.AUTO.getValue() + "");
            executor.setUpdateTime(new Date());
            jobExecutorMapper.insertSelective(executor);
        }
        return ResultT.SUCCESS;
    }

    @Override
    public ResultT<String> registryRemove(RegistryParam registryParam) {
        // 直接删除
        jobExecutorMapper.delete(
                deleteFrom(jobExecutor)
                        .where(groupUuid, isEqualTo(registryParam.getGroupUuid()))
                        .and(address, isEqualTo(registryParam.getExecutorAddress()))
                        .build().render(RenderingStrategies.MYBATIS3)
        );
        return ResultT.SUCCESS;
    }
}
