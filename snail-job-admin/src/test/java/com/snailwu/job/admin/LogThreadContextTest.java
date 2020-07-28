package com.snailwu.job.admin;

import com.snailwu.job.admin.constant.JobHttpConstants;
import org.apache.logging.log4j.ThreadContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author 吴庆龙
 * @date 2020/7/28 7:40
 */
public class LogThreadContextTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(LogThreadContextTest.class);

    public static void main(String[] args) {
        ThreadContext.put(JobHttpConstants.JOB_LOG_ID, "WuQinglong");
        LOGGER.info("Hello Log");
        LOGGER.error("Hello Log");
    }
}
