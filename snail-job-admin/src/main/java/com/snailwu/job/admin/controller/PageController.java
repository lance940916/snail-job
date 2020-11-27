package com.snailwu.job.admin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author 吴庆龙
 * @date 2020/11/26 下午5:58
 */
@Controller
public class PageController {

    @RequestMapping({"/", "index"})
    public String index() {
        return "index";
    }

    @RequestMapping("/group-page")
    public String groupPage() {
        return "group/index";
    }

    @RequestMapping("/info-page")
    public String infoPage() {
        return "info/index";
    }

    @RequestMapping("/log-page")
    public String logPage() {
        return "log/index";
    }

}
