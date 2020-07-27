package com.snailwu.job.admin.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.snailwu.job.admin.core.model.JobGroup;
import com.snailwu.job.admin.mapper.JobGroupDynamicSqlSupport;
import com.snailwu.job.admin.mapper.JobGroupMapper;
import org.apache.commons.lang3.Validate;
import org.mybatis.dynamic.sql.render.RenderingStrategies;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import static org.mybatis.dynamic.sql.SqlBuilder.*;

/**
 * @author 吴庆龙
 * @date 2020/7/23 3:14 下午
 */
@Service
public class GroupService {
    private static final Logger LOGGER = LoggerFactory.getLogger(GroupService.class);

    @Resource
    private JobGroupMapper jobGroupMapper;

    /**
     * 分页列表
     */
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

    /**
     * 保存或更新
     */
    public void saveOrUpdate(JobGroup group) {
        // 校验字段
        Validate.notBlank(group.getTitle(), "标题不能为空");
        Validate.notBlank(group.getName(), "名称不能为空");

        if (group.getId() == null) {
            // 校验 name 是否有相同的
            long count = jobGroupMapper.count(
                    select(count(JobGroupDynamicSqlSupport.id))
                            .from(JobGroupDynamicSqlSupport.jobGroup)
                            .where(JobGroupDynamicSqlSupport.name, isEqualTo(group.getName()))
                            .build().render(RenderingStrategies.MYBATIS3)
            );
            Validate.isTrue(count == 0, "groupName已经存在.");
            int ret = jobGroupMapper.insertSelective(group);
            if (ret == 1) {
                LOGGER.info("保存成功");
            } else {
                LOGGER.info("保存失败");
            }
        } else {
            int ret = jobGroupMapper.updateByPrimaryKeySelective(group);
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
        int ret = jobGroupMapper.deleteByPrimaryKey(id);
        if (ret == 1) {
            LOGGER.info("删除成功");
        } else {
            LOGGER.info("删除失败,记录不存在");
        }
    }
}
