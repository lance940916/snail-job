package com.snailwu.job.admin.controller;

import com.snailwu.job.admin.core.model.JobGroup;
import com.snailwu.job.admin.service.GroupService;
import com.snailwu.job.core.biz.model.ResultT;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author 吴庆龙
 * @date 2020/7/23 10:34 上午
 */
@Controller
@RequestMapping("/group")
public class GroupController {

    @Resource
    private GroupService groupService;

    /**
     * 主页
     */
    @RequestMapping
    public String index() {
        return "group/group.index";
    }

    /**
     * 新增
     */
    @PostMapping("/save")
    @ResponseBody
    public String save() {
        return "";
    }

    /**
     * 更新
     */
    @DeleteMapping("/update")
    @ResponseBody
    public ResultT<String> update() {
        return ResultT.SUCCESS;
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete")
    @ResponseBody
    public ResultT<String> delete() {
        return ResultT.SUCCESS;
    }

    /**
     * 查询
     */
    @GetMapping("/get")
    @ResponseBody
    public ResultT<JobGroup> get() {

        return new ResultT<>(null);
    }

    /**
     * 列表
     */
    @GetMapping("/list")
    @ResponseBody
    public ResultT<List<JobGroup>> list() {

        return new ResultT<>(null);
    }

}
