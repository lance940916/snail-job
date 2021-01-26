package com.snailwu.job.admin.service;

import com.github.pagehelper.PageInfo;
import com.github.pagehelper.page.PageMethod;
import com.snailwu.job.admin.controller.request.JobGroupSearchRequest;
import com.snailwu.job.admin.mapper.JobAppDynamicSqlSupport;
import com.snailwu.job.admin.mapper.JobAppMapper;
import com.snailwu.job.admin.model.JobApp;
import org.apache.commons.lang3.Validate;
import org.mybatis.dynamic.sql.render.RenderingStrategies;
import org.mybatis.dynamic.sql.select.render.SelectStatementProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

import static org.mybatis.dynamic.sql.SqlBuilder.*;

/**
 * @author 吴庆龙
 * @date 2020/7/23 3:14 下午
 */
@Service
public class AppService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AppService.class);

    @Resource
    private JobAppMapper jobAppMapper;

    /**
     * 分页列表
     */
    public PageInfo<JobApp> list(JobGroupSearchRequest searchRequest) {
        SelectStatementProvider statementProvider = select(JobAppDynamicSqlSupport.jobApp.allColumns())
                .from(JobAppDynamicSqlSupport.jobApp)
                .where()
                .and(JobAppDynamicSqlSupport.title, isLikeWhenPresent(searchRequest.getTitle()))
                .and(JobAppDynamicSqlSupport.appName, isLikeWhenPresent(searchRequest.getAppName()))
                .build()
                .render(RenderingStrategies.MYBATIS3);
        return PageMethod.startPage(searchRequest.getPage(), searchRequest.getLimit())
                .doSelectPageInfo(() -> jobAppMapper.selectMany(statementProvider));
    }

    /**
     * 保存或更新
     */
    public void saveOrUpdate(JobApp app) {
        if (app.getId() == null) {
            // 校验 name 是否有相同的
            long count = jobAppMapper.count(
                    select(count(JobAppDynamicSqlSupport.id))
                            .from(JobAppDynamicSqlSupport.jobApp)
                            .where(JobAppDynamicSqlSupport.appName, isEqualTo(app.getAppName()))
                            .build()
                            .render(RenderingStrategies.MYBATIS3)
            );
            Validate.isTrue(count == 0, "AppName已经存在.");
            int ret = jobAppMapper.insertSelective(app);
            if (ret == 1) {
                LOGGER.info("保存成功");
            } else {
                LOGGER.info("保存失败");
            }
        } else {
            app.setUpdateTime(new Date());
            int ret = jobAppMapper.updateByPrimaryKeySelective(app);
            if (ret == 1) {
                LOGGER.info("更新成功");
            } else {
                LOGGER.info("更新失败");
            }
        }
    }

    /**
     * 删除
     */
    public void delete(Integer id) {
        int ret = jobAppMapper.deleteByPrimaryKey(id);
        if (ret == 1) {
            LOGGER.info("删除成功");
        } else {
            LOGGER.info("删除失败,记录不存在");
        }
    }

    /**
     * 查询所有分组，供下拉框使用
     */
    public List<JobApp> listAll() {
        return jobAppMapper.selectMany(
                select(JobAppDynamicSqlSupport.id, JobAppDynamicSqlSupport.appName,
                        JobAppDynamicSqlSupport.title)
                        .from(JobAppDynamicSqlSupport.jobApp)
                        .build()
                        .render(RenderingStrategies.MYBATIS3)
        );
    }
}
