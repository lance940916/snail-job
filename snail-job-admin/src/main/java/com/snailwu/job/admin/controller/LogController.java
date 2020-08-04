package com.snailwu.job.admin.controller;

import com.github.pagehelper.PageInfo;
import com.snailwu.job.admin.core.model.JobInfo;
import com.snailwu.job.admin.service.LogService;
import com.snailwu.job.core.biz.model.ResultT;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author 吴庆龙
 * @date 2020/7/23 3:12 下午
 */
@RestController
@RequestMapping("/log")
public class LogController {

    @Resource
    private LogService logService;

    /**
     * 分页查询
     */
    @GetMapping
    public ResultT<PageInfo<JobInfo>> list(Integer pageNum, Integer pageSize) {
        PageInfo<JobInfo> pageInfo = logService.list(pageNum, pageSize);
        return new ResultT<>(pageInfo);
    }

    /**
     * 新增
     */
    @PostMapping
    public ResultT<String> save(JobInfo jobInfo) {
        logService.saveOrUpdate(jobInfo);
        return ResultT.SUCCESS;
    }

    /**
     * 更新
     */
    @PutMapping
    public ResultT<String> update(JobInfo jobInfo) {
        logService.saveOrUpdate(jobInfo);
        return ResultT.SUCCESS;
    }

    /**
     * 删除
     */
    @DeleteMapping
    public ResultT<String> delete(Integer id) {
        logService.delete(id);
        return ResultT.SUCCESS;
    }

}
