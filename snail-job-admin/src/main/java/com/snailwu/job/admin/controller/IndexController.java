package com.snailwu.job.admin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author 吴庆龙
 * @date 2020/6/4 11:51 上午
 */
@Controller
public class IndexController {

    @RequestMapping("/")
    public String index(Model model) {
        return "index";
    }

    @RequestMapping("/group")
    public String group(Model model) {
        return "group/group.index";
    }

    @RequestMapping("/job")
    public String job(Model model) {
        return "job/job.index";
    }

    @RequestMapping("/log")
    public String log(Model model) {
        return "log/log.index";
    }

}
