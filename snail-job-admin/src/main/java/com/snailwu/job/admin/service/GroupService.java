package com.snailwu.job.admin.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.snailwu.job.admin.core.model.JobGroup;
import com.snailwu.job.admin.mapper.JobGroupDynamicSqlSupport;
import com.snailwu.job.admin.mapper.JobGroupMapper;
import org.mybatis.dynamic.sql.render.RenderingStrategies;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import static org.mybatis.dynamic.sql.SqlBuilder.select;

/**
 * @author 吴庆龙
 * @date 2020/7/23 3:14 下午
 */
@Service
public class GroupService {

    @Resource
    private JobGroupMapper jobGroupMapper;

    public void save() {

    }

    public PageInfo<JobGroup> list(Integer pageNum, Integer pageSize) {
        return PageHelper.startPage(pageNum, pageSize)
                .doSelectPageInfo(() -> {
                    jobGroupMapper.selectMany(
                            select(JobGroupDynamicSqlSupport.jobGroup.allColumns())
                                    .from(JobGroupDynamicSqlSupport.jobGroup)
                                    .build().render(RenderingStrategies.MYBATIS3)
                    );
                });
    }
}
