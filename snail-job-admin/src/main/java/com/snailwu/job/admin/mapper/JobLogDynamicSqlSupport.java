package com.snailwu.job.admin.mapper;

import org.mybatis.dynamic.sql.SqlColumn;
import org.mybatis.dynamic.sql.SqlTable;

import javax.annotation.Generated;
import java.sql.JDBCType;
import java.util.Date;

public final class JobLogDynamicSqlSupport {
    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final JobLog jobLog = new JobLog();

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<Long> id = jobLog.id;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<Integer> jobId = jobLog.jobId;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<String> appName = jobLog.appName;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<String> execAddress = jobLog.execAddress;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<String> execHandler = jobLog.execHandler;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<String> execParam = jobLog.execParam;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<Byte> failRetryCount = jobLog.failRetryCount;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<Date> triggerTime = jobLog.triggerTime;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<Integer> triggerCode = jobLog.triggerCode;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<String> triggerMsg = jobLog.triggerMsg;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<Date> execTime = jobLog.execTime;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<Integer> execCode = jobLog.execCode;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<String> execMsg = jobLog.execMsg;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<Byte> alarmStatus = jobLog.alarmStatus;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final class JobLog extends SqlTable {
        public final SqlColumn<Long> id = column("id", JDBCType.BIGINT);

        public final SqlColumn<Integer> jobId = column("job_id", JDBCType.INTEGER);

        public final SqlColumn<String> appName = column("app_name", JDBCType.VARCHAR);

        public final SqlColumn<String> execAddress = column("exec_address", JDBCType.VARCHAR);

        public final SqlColumn<String> execHandler = column("exec_handler", JDBCType.VARCHAR);

        public final SqlColumn<String> execParam = column("exec_param", JDBCType.VARCHAR);

        public final SqlColumn<Byte> failRetryCount = column("fail_retry_count", JDBCType.TINYINT);

        public final SqlColumn<Date> triggerTime = column("trigger_time", JDBCType.TIMESTAMP);

        public final SqlColumn<Integer> triggerCode = column("trigger_code", JDBCType.INTEGER);

        public final SqlColumn<String> triggerMsg = column("trigger_msg", JDBCType.VARCHAR);

        public final SqlColumn<Date> execTime = column("exec_time", JDBCType.TIMESTAMP);

        public final SqlColumn<Integer> execCode = column("exec_code", JDBCType.INTEGER);

        public final SqlColumn<String> execMsg = column("exec_msg", JDBCType.VARCHAR);

        public final SqlColumn<Byte> alarmStatus = column("alarm_status", JDBCType.TINYINT);

        public JobLog() {
            super("job_log");
        }
    }
}