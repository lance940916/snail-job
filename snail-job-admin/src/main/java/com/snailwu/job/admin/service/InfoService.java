package com.snailwu.job.admin.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.snailwu.job.admin.core.model.JobInfo;
import com.snailwu.job.admin.mapper.JobInfoDynamicSqlSupport;
import com.snailwu.job.admin.mapper.JobInfoMapper;
import org.mybatis.dynamic.sql.render.RenderingStrategies;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import static org.mybatis.dynamic.sql.SqlBuilder.select;

/**
 * @author 吴庆龙
 * @date 2020/7/27 10:37 上午
 */
@Service
public class InfoService {
    private static final Logger LOGGER = LoggerFactory.getLogger(InfoService.class);

    @Resource
    private JobInfoMapper jobInfoMapper;

    /**
     * 分页查询
     */
    public PageInfo<JobInfo> list(Integer pageNum, Integer pageSize) {
        return PageHelper.startPage(pageNum, pageSize)
                .doSelectPageInfo(() -> {
                    jobInfoMapper.selectMany(
                            select(JobInfoDynamicSqlSupport.jobInfo.allColumns())
                            .from(JobInfoDynamicSqlSupport.jobInfo)
                            .build().render(RenderingStrategies.MYBATIS3)
                    );
                });
    }

    /**
     * 保存 或 更新
     */
    public void saveOrUpdate(JobInfo jobInfo) {


    }

    /**
     * 删除
     */
    public void delete(Integer id) {
        int ret = jobInfoMapper.deleteByPrimaryKey(id);
        if (ret == 1) {
            LOGGER.info("删除成功");
        } else {
            LOGGER.info("删除失败,记录不存在");
        }
    }

}
