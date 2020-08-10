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
    public static final SqlColumn<String> groupName = jobInfo.groupName;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<String> cron = jobInfo.cron;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<String> description = jobInfo.description;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<Date> createTime = jobInfo.createTime;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<Date> updateTime = jobInfo.updateTime;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<String> author = jobInfo.author;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<String> alarmEmail = jobInfo.alarmEmail;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<String> executorRouteStrategy = jobInfo.executorRouteStrategy;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<String> executorHandler = jobInfo.executorHandler;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<String> executorParam = jobInfo.executorParam;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<Integer> executorTimeout = jobInfo.executorTimeout;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<Byte> executorFailRetryCount = jobInfo.executorFailRetryCount;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<Byte> triggerStatus = jobInfo.triggerStatus;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<Long> triggerLastTime = jobInfo.triggerLastTime;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<Long> triggerNextTime = jobInfo.triggerNextTime;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final class JobInfo extends SqlTable {
        public final SqlColumn<Integer> id = column("id", JDBCType.INTEGER);

        public final SqlColumn<String> groupName = column("group_name", JDBCType.VARCHAR);

        public final SqlColumn<String> cron = column("cron", JDBCType.VARCHAR);

        public final SqlColumn<String> description = column("description", JDBCType.VARCHAR);

        public final SqlColumn<Date> createTime = column("create_time", JDBCType.TIMESTAMP);

        public final SqlColumn<Date> updateTime = column("update_time", JDBCType.TIMESTAMP);

        public final SqlColumn<String> author = column("author", JDBCType.VARCHAR);

        public final SqlColumn<String> alarmEmail = column("alarm_email", JDBCType.VARCHAR);

        public final SqlColumn<String> executorRouteStrategy = column("executor_route_strategy", JDBCType.VARCHAR);

        public final SqlColumn<String> executorHandler = column("executor_handler", JDBCType.VARCHAR);

        public final SqlColumn<String> executorParam = column("executor_param", JDBCType.VARCHAR);

        public final SqlColumn<Integer> executorTimeout = column("executor_timeout", JDBCType.INTEGER);

        public final SqlColumn<Byte> executorFailRetryCount = column("executor_fail_retry_count", JDBCType.TINYINT);

        public final SqlColumn<Byte> triggerStatus = column("trigger_status", JDBCType.TINYINT);

        public final SqlColumn<Long> triggerLastTime = column("trigger_last_time", JDBCType.BIGINT);

        public final SqlColumn<Long> triggerNextTime = column("trigger_next_time", JDBCType.BIGINT);

        public JobInfo() {
            super("job_info");
        }
    }
}