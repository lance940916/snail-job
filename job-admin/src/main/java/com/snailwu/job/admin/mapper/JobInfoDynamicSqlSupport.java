package com.snailwu.job.admin.mapper;

import org.mybatis.dynamic.sql.SqlColumn;
import org.mybatis.dynamic.sql.SqlTable;

import javax.annotation.Generated;
import java.sql.JDBCType;
import java.util.Date;

public final class JobInfoDynamicSqlSupport {
    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final JobInfo jobInfo = new JobInfo();

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<Integer> id = jobInfo.id;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<String> name = jobInfo.name;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<String> appName = jobInfo.appName;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<String> cron = jobInfo.cron;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<Date> createTime = jobInfo.createTime;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<Date> updateTime = jobInfo.updateTime;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<String> author = jobInfo.author;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<String> alarmEmail = jobInfo.alarmEmail;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<String> execRouteStrategy = jobInfo.execRouteStrategy;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<String> execHandler = jobInfo.execHandler;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<String> execParam = jobInfo.execParam;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<Integer> execTimeout = jobInfo.execTimeout;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<Byte> execFailRetryCount = jobInfo.execFailRetryCount;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<Byte> triggerStatus = jobInfo.triggerStatus;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<Long> triggerLastTime = jobInfo.triggerLastTime;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<Long> triggerNextTime = jobInfo.triggerNextTime;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final class JobInfo extends SqlTable {
        public final SqlColumn<Integer> id = column("id", JDBCType.INTEGER);

        public final SqlColumn<String> name = column("name", JDBCType.VARCHAR);

        public final SqlColumn<String> appName = column("app_name", JDBCType.VARCHAR);

        public final SqlColumn<String> cron = column("cron", JDBCType.VARCHAR);

        public final SqlColumn<Date> createTime = column("create_time", JDBCType.TIMESTAMP);

        public final SqlColumn<Date> updateTime = column("update_time", JDBCType.TIMESTAMP);

        public final SqlColumn<String> author = column("author", JDBCType.VARCHAR);

        public final SqlColumn<String> alarmEmail = column("alarm_email", JDBCType.VARCHAR);

        public final SqlColumn<String> execRouteStrategy = column("exec_route_strategy", JDBCType.VARCHAR);

        public final SqlColumn<String> execHandler = column("exec_handler", JDBCType.VARCHAR);

        public final SqlColumn<String> execParam = column("exec_param", JDBCType.VARCHAR);

        public final SqlColumn<Integer> execTimeout = column("exec_timeout", JDBCType.INTEGER);

        public final SqlColumn<Byte> execFailRetryCount = column("exec_fail_retry_count", JDBCType.TINYINT);

        public final SqlColumn<Byte> triggerStatus = column("trigger_status", JDBCType.TINYINT);

        public final SqlColumn<Long> triggerLastTime = column("trigger_last_time", JDBCType.BIGINT);

        public final SqlColumn<Long> triggerNextTime = column("trigger_next_time", JDBCType.BIGINT);

        public JobInfo() {
            super("job_info");
        }
    }
}