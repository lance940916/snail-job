package com.snailwu.job.admin.service;

import com.snailwu.job.admin.mapper.JobLogMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author 吴庆龙
 * @date 2020/7/27 10:37 上午
 */
@Service
public class LogService {
    private static final Logger LOGGER = LoggerFactory.getLogger(LogService.class);

    @Resource
    private JobLogMapper jobLogMapper;


}
