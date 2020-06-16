package com.snailwu.job.admin.service.impl;

import com.snailwu.job.admin.core.model.JobExecutorNode;
import com.snailwu.job.admin.mapper.JobExecutorNodeMapper;
import com.snailwu.job.core.biz.AdminBiz;
import com.snailwu.job.core.biz.model.CallbackParam;
import com.snailwu.job.core.biz.model.RegistryParam;
import com.snailwu.job.core.biz.model.ResultT;
import org.mybatis.dynamic.sql.render.RenderingStrategies;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

import static com.snailwu.job.admin.mapper.JobExecutorNodeDynamicSqlSupport.*;
import static org.mybatis.dynamic.sql.SqlBuilder.*;

/**
 * @author 吴庆龙
 * @date 2020/6/3 11:34 上午
 */
@Service
public class AdminBizImpl implements AdminBiz {
    private static final Logger log = LoggerFactory.getLogger(AdminBizImpl.class);

    @Resource
    private JobExecutorNodeMapper jobExecutorNodeMapper;

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
        int update = jobExecutorNodeMapper.update(
                update(jobExecutorNode)
                .set(updateTime).equalTo(new Date())
                .where(appName, isEqualTo(registryParam.getAppName()))
                .and(address, isEqualTo(registryParam.getAddress()))
                .build().render(RenderingStrategies.MYBATIS3)
        );

        // 数据不存在，插入数据
        if (update < 1) {
            JobExecutorNode node = new JobExecutorNode();
            node.setAppName(registryParam.getAppName());
            node.setAddress(registryParam.getAddress());
            node.setUpdateTime(new Date());
            jobExecutorNodeMapper.insertSelective(node);
        }
        return ResultT.SUCCESS;
    }

    @Override
    public ResultT<String> registryRemove(RegistryParam registryParam) {
        // 直接删除
        jobExecutorNodeMapper.delete(
                deleteFrom(jobExecutorNode)
                .where(appName, isEqualTo(registryParam.getAppName()))
                .and(address, isEqualTo(registryParam.getAddress()))
                .build().render(RenderingStrategies.MYBATIS3)
        );
        return ResultT.SUCCESS;
    }
}
