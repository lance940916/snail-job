package com.snailwu.job.admin.controller;

import com.snailwu.job.core.biz.AdminBiz;
import com.snailwu.job.core.biz.model.ResultT;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 接收 Worker 节点的请求
 *
 * @author 吴庆龙
 * @date 2020/6/3 11:31 上午
 */
@RestController
@RequestMapping("/snail/job/api")
public class JobApiController {
    @Resource
    private AdminBiz adminBiz;

    @PostMapping("/callback")
    public ResultT<String> callback() {
        // 反序列化

        return ResultT.SUCCESS;
    }

    @PostMapping("registry")
    public ResultT<String> registry() {
        // 反序列化

        return ResultT.SUCCESS;
    }

    @PostMapping("registryRemove")
    public ResultT<String> registryRemove() {
        // 反序列化

        return ResultT.SUCCESS;
    }
}
