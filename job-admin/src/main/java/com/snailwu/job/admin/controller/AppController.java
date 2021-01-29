package com.snailwu.job.admin.controller;

import com.github.pagehelper.PageInfo;
import com.snailwu.job.admin.controller.request.JobGroupSearchRequest;
import com.snailwu.job.admin.model.JobApp;
import com.snailwu.job.admin.service.AppService;
import com.snailwu.job.core.biz.model.ResultT;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author 吴庆龙
 * @date 2020/7/23 10:34 上午
 */
@RestController
@RequestMapping("/app")
public class AppController {

    @Resource
    private AppService appService;

    /**
     * 分页列表
     */
    @GetMapping
    public ResultT<PageInfo<JobApp>> list(JobGroupSearchRequest searchRequest) {
        PageInfo<JobApp> pageInfo = appService.list(searchRequest);
        return new ResultT<>(pageInfo);
    }

    /**
     * 新增
     */
    @PostMapping
    public ResultT<String> save(@Validated @RequestBody JobApp jobApp) {
        appService.saveOrUpdate(jobApp);
        return ResultT.SUCCESS;
    }

    /**
     * 更新
     */
    @PutMapping
    public ResultT<String> update(@Validated @RequestBody JobApp jobApp) {
        appService.saveOrUpdate(jobApp);
        return ResultT.SUCCESS;
    }

    /**
     * 删除
     */
    @DeleteMapping("/{id}")
    public ResultT<String> delete(@PathVariable("id") Integer id) {
        appService.delete(id);
        return ResultT.SUCCESS;
    }

    @GetMapping("/list_all")
    public ResultT<List<JobApp>> listAll() {
        List<JobApp> list = appService.listAll();
        return new ResultT<>(list);
    }

}
