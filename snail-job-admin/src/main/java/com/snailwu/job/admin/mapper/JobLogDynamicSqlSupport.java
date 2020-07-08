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
    public static final SqlColumn<String> groupName = jobLog.groupName;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<String> executorAddress = jobLog.executorAddress;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<String> executorHandler = jobLog.executorHandler;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<String> executorParam = jobLog.executorParam;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<Byte> executorFailRetryCount = jobLog.executorFailRetryCount;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<Date> triggerTime = jobLog.triggerTime;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<Integer> triggerCode = jobLog.triggerCode;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<Date> execTime = jobLog.execTime;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<Integer> execCode = jobLog.execCode;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<Byte> alarmStatus = jobLog.alarmStatus;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<String> triggerMsg = jobLog.triggerMsg;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<String> execLog = jobLog.execLog;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final class JobLog extends SqlTable {
        public final SqlColumn<Long> id = column("id", JDBCType.BIGINT);

        public final SqlColumn<Integer> jobId = column("job_id", JDBCType.INTEGER);

        public final SqlColumn<String> groupName = column("group_name", JDBCType.VARCHAR);

        public final SqlColumn<String> executorAddress = column("executor_address", JDBCType.VARCHAR);

        public final SqlColumn<String> executorHandler = column("executor_handler", JDBCType.VARCHAR);

        public final SqlColumn<String> executorParam = column("executor_param", JDBCType.VARCHAR);

        public final SqlColumn<Byte> executorFailRetryCount = column("executor_fail_retry_count", JDBCType.TINYINT);

        public final SqlColumn<Date> triggerTime = column("trigger_time", JDBCType.TIMESTAMP);

        public final SqlColumn<Integer> triggerCode = column("trigger_code", JDBCType.INTEGER);

        public final SqlColumn<Date> execTime = column("exec_time", JDBCType.TIMESTAMP);

        public final SqlColumn<Integer> execCode = column("exec_code", JDBCType.INTEGER);

        public final SqlColumn<Byte> alarmStatus = column("alarm_status", JDBCType.TINYINT);

        public final SqlColumn<String> triggerMsg = column("trigger_msg", JDBCType.LONGVARCHAR);

        public final SqlColumn<String> execLog = column("exec_log", JDBCType.LONGVARCHAR);

        public JobLog() {
            super("job_log");
        }
    }
}