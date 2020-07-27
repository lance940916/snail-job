package com.snailwu.job.admin.controller;

import com.snailwu.job.admin.vo.SystemStatusVO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author 吴庆龙
 * @date 2020/6/4 11:51 上午
 */
@Controller
public class IndexController {

    @GetMapping("/")
    public String status(Model model) {
        SystemStatusVO vo = new SystemStatusVO();
        vo.setTotalJobAmount(8);
        vo.setTotalGroupAmount(2);
        vo.setTotalInvokeTimes(10);
        model.addAttribute("status", vo);
        return "index";
    }

}
