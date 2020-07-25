package com.snailwu.job.admin.controller;

import com.snailwu.job.admin.service.GroupService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author 吴庆龙
 * @date 2020/7/23 10:34 上午
 */
@Controller
@RequestMapping("/group")
public class GroupController {

    @Resource
    private GroupService groupService;

    @GetMapping
    public String index(Model model,
                        @RequestParam(name = "page_num", required = false, defaultValue = "1") Integer pageNum,
                        @RequestParam(name = "pageSize", required = false, defaultValue = "10") Integer pageSize) {
//        PageInfo<JobGroup> list = groupService.list(pageNum, pageSize);
//        model.addAttribute("groupList", list);
        return "group/group.index";
    }

    /**
     * 新增
     */
    @PostMapping("/save")
    public String save() {
        return "";
    }

    /**
     * 更新
     */
    @DeleteMapping("/update")
    public String update() {
        return "";
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete")
    public String delete() {
        return "";
    }

    /**
     * 查询
     */
    @GetMapping("/get")
    public String get() {
        return "";
    }

}
