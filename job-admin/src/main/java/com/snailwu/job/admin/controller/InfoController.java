package com.snailwu.job.admin.controller;

import com.github.pagehelper.PageInfo;
import com.snailwu.job.admin.controller.request.JobInfoEditRequest;
import com.snailwu.job.admin.controller.request.JobInfoSearchRequest;
import com.snailwu.job.admin.controller.vo.RouteVO;
import com.snailwu.job.admin.core.cron.CronExpression;
import com.snailwu.job.admin.core.route.ExecRouteStrategyEnum;
import com.snailwu.job.admin.core.thread.JobTriggerPoolHelper;
import com.snailwu.job.admin.core.trigger.TriggerTypeEnum;
import com.snailwu.job.admin.model.JobInfo;
import com.snailwu.job.admin.service.InfoService;
import com.snailwu.job.core.biz.model.ResultT;
import com.snailwu.job.core.exception.JobException;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.snailwu.job.admin.constant.AdminConstants.DATE_TIME_PATTERN;

/**
 * @author 吴庆龙
 * @date 2020/7/23 3:12 下午
 */
@RestController
@RequestMapping("/info")
public class InfoController {

    @Resource
    private InfoService infoService;

    /**
     * 分页查询
     */
    @GetMapping
    public ResultT<PageInfo<JobInfo>> list(JobInfoSearchRequest searchRequest) {
        PageInfo<JobInfo> pageInfo = infoService.list(searchRequest);
        return new ResultT<>(pageInfo);
    }

    /**
     * 新增
     */
    @PostMapping
    public ResultT<String> save(@Validated @RequestBody JobInfoEditRequest editRequest) {
        infoService.saveOrUpdate(editRequest.convertToJobInfo());
        return ResultT.SUCCESS;
    }

    /**
     * 更新
     */
    @PutMapping
    public ResultT<String> update(@RequestBody JobInfo jobInfo) {
        infoService.saveOrUpdate(jobInfo);
        return ResultT.SUCCESS;
    }

    /**
     * 删除
     */
    @DeleteMapping("/{id}")
    public ResultT<String> delete(@PathVariable("id") Integer id) {
        infoService.delete(id);
        return ResultT.SUCCESS;
    }

    /**
     * 启动任务
     */
    @PostMapping("/start/{id}")
    public ResultT<String> start(@PathVariable("id") Integer id) {
        infoService.start(id);
        return ResultT.SUCCESS;
    }

    /**
     * 停止任务
     */
    @PostMapping("/stop/{id}")
    public ResultT<String> stop(@PathVariable("id") Integer id) {
        infoService.stop(id);
        return ResultT.SUCCESS;
    }

    /**
     * 复制
     */
    @PostMapping("/copy/{id}")
    public ResultT<String> copy(@PathVariable("id") Integer id) {
        infoService.copy(id);
        return ResultT.SUCCESS;
    }

    /**
     * 执行一次
     */
    @PostMapping("/exec/{id}")
    public ResultT<String> exec(@PathVariable("id") Integer id, @RequestParam("exec_param") String execParam) {
        JobTriggerPoolHelper.push(id, TriggerTypeEnum.API, -1, execParam);
        return ResultT.SUCCESS;
    }

    /**
     * 获取路由策略
     */
    @GetMapping("/list_route")
    public ResultT<List<RouteVO>> listRoute() {
        List<RouteVO> list = new ArrayList<>();
        ExecRouteStrategyEnum[] routeStrategyEnums = ExecRouteStrategyEnum.values();
        for (ExecRouteStrategyEnum strategyEnum : routeStrategyEnums) {
            list.add(new RouteVO(strategyEnum.getName(), strategyEnum.getDesc()));
        }
        return new ResultT<>(list);
    }

    /**
     * 下次执行时间
     */
    @GetMapping("/next_trigger_time")
    public ResultT<List<String>> nextTriggerTime(String cron) {
        List<String> list = new ArrayList<>();
        try {
            CronExpression expression = new CronExpression(cron);
            Date curDate = new Date();
            for (int i = 0; i < 5; i++) {
                Date nextTriggerTime = expression.getNextValidTimeAfter(curDate);
                list.add(DateFormatUtils.format(nextTriggerTime, DATE_TIME_PATTERN));
                curDate = nextTriggerTime;
            }
            return new ResultT<>(list);
        } catch (ParseException e) {
            throw new JobException("解析Cron表达式异常");
        }
    }

    @GetMapping("/list_all")
    public ResultT<List<JobInfo>> listAll(String groupName) {
        List<JobInfo> list = infoService.listAll(groupName);
        return new ResultT<>(list);
    }

}
