package com.snail.job.smaple.task;

import com.snailwu.job.core.biz.model.ResultT;
import com.snailwu.job.core.handler.annotation.SnailJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author 吴庆龙
 * @date 2020/8/7 3:05 下午
 */
@Component
public class SampleTask {
    private static final Logger LOGGER = LoggerFactory.getLogger(SampleTask.class);

    @SnailJob(name = "default-job", init = "beforeMethod", destroy = "afterMethod")
    public ResultT<String> defaultJob(String param) {
        LOGGER.info("执行默认任务");
        return ResultT.SUCCESS;
    }

    public void beforeMethod() {
        System.out.println("前置方法");
    }

    public void afterMethod() {
        System.out.println("前置方法");
    }

}
