package com.snailwu.job.admin.controller;

import com.github.pagehelper.PageInfo;
import com.snailwu.job.admin.controller.entity.JobLogVO;
import com.snailwu.job.admin.controller.request.JobLogDeleteRequest;
import com.snailwu.job.admin.controller.request.JobLogSearchRequest;
import com.snailwu.job.admin.service.LogService;
import com.snailwu.job.core.biz.model.ResultT;
import org.springframework.validation.annotation.Validated;
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
    public ResultT<PageInfo<JobLogVO>> list(JobLogSearchRequest searchRequest) {
        PageInfo<JobLogVO> pageInfo = logService.list(searchRequest);
        return new ResultT<>(pageInfo);
    }

    /**
     * 删除日志
     */
    @PostMapping("/batch_delete")
    public ResultT<String> deleteByTime(@Validated @RequestBody JobLogDeleteRequest deleteRequest) {
        logService.deleteByTime(deleteRequest);
        return ResultT.SUCCESS;
    }

    /**
     * 终止任务
     */
    @PostMapping("/kill/{id}")
    public ResultT<String> killTrigger(@PathVariable("id") Long logId) {
        return logService.killTrigger(logId);
    }

}
