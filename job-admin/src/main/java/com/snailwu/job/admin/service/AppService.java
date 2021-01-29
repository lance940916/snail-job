package com.snailwu.job.admin.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.page.PageMethod;
import com.snailwu.job.admin.controller.request.JobGroupSearchRequest;
import com.snailwu.job.admin.mapper.JobAppMapper;
import com.snailwu.job.admin.model.JobApp;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

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
        return PageHelper.startPage(searchRequest.getPage(), searchRequest.getLimit())
                .doSelectPageInfo(() -> jobAppMapper.selectByCondition(searchRequest));
    }

    /**
     * 保存或更新
     */
    public void saveOrUpdate(JobApp app) {
        if (app.getId() == null) {
            // 校验 name 是否有相同的
            JobApp jobApp = jobAppMapper.selectByAppName(app.getAppName());
            Validate.isTrue(jobApp == null, "AppName已经存在.");
            jobAppMapper.insert(app);
        } else {
            app.setUpdateTime(new Date());
            jobAppMapper.updateByPrimaryKey(app);
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
        return jobAppMapper.selectAll();
    }
}
