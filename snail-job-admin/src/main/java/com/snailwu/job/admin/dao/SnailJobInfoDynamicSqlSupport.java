package com.snailwu.job.admin.dao;

import java.sql.JDBCType;
import java.util.Date;
import javax.annotation.Generated;
import org.mybatis.dynamic.sql.SqlColumn;
import org.mybatis.dynamic.sql.SqlTable;

public final class SnailJobInfoDynamicSqlSupport {
    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SnailJobInfo snailJobInfo = new SnailJobInfo();

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<Integer> id = snailJobInfo.id;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<Integer> jobGroup = snailJobInfo.jobGroup;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<String> jobCron = snailJobInfo.jobCron;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<String> jobDesc = snailJobInfo.jobDesc;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<Date> addTime = snailJobInfo.addTime;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<Date> updateTime = snailJobInfo.updateTime;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<String> author = snailJobInfo.author;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<String> alarmEmail = snailJobInfo.alarmEmail;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<String> executorRouteStrategy = snailJobInfo.executorRouteStrategy;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<String> executorHandler = snailJobInfo.executorHandler;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<String> executorParam = snailJobInfo.executorParam;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<String> executorBlockStrategy = snailJobInfo.executorBlockStrategy;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<Integer> executorTimeout = snailJobInfo.executorTimeout;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<Integer> executorFailRetryCount = snailJobInfo.executorFailRetryCount;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<String> glueType = snailJobInfo.glueType;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<String> glueRemark = snailJobInfo.glueRemark;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<Date> glueUpdatetime = snailJobInfo.glueUpdatetime;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<String> childJobid = snailJobInfo.childJobid;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<Byte> triggerStatus = snailJobInfo.triggerStatus;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<Long> triggerLastTime = snailJobInfo.triggerLastTime;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<Long> triggerNextTime = snailJobInfo.triggerNextTime;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<String> glueSource = snailJobInfo.glueSource;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final class SnailJobInfo extends SqlTable {
        public final SqlColumn<Integer> id = column("id", JDBCType.INTEGER);

        public final SqlColumn<Integer> jobGroup = column("job_group", JDBCType.INTEGER);

        public final SqlColumn<String> jobCron = column("job_cron", JDBCType.VARCHAR);

        public final SqlColumn<String> jobDesc = column("job_desc", JDBCType.VARCHAR);

        public final SqlColumn<Date> addTime = column("add_time", JDBCType.TIMESTAMP);

        public final SqlColumn<Date> updateTime = column("update_time", JDBCType.TIMESTAMP);

        public final SqlColumn<String> author = column("author", JDBCType.VARCHAR);

        public final SqlColumn<String> alarmEmail = column("alarm_email", JDBCType.VARCHAR);

        public final SqlColumn<String> executorRouteStrategy = column("executor_route_strategy", JDBCType.VARCHAR);

        public final SqlColumn<String> executorHandler = column("executor_handler", JDBCType.VARCHAR);

        public final SqlColumn<String> executorParam = column("executor_param", JDBCType.VARCHAR);

        public final SqlColumn<String> executorBlockStrategy = column("executor_block_strategy", JDBCType.VARCHAR);

        public final SqlColumn<Integer> executorTimeout = column("executor_timeout", JDBCType.INTEGER);

        public final SqlColumn<Integer> executorFailRetryCount = column("executor_fail_retry_count", JDBCType.INTEGER);

        public final SqlColumn<String> glueType = column("glue_type", JDBCType.VARCHAR);

        public final SqlColumn<String> glueRemark = column("glue_remark", JDBCType.VARCHAR);

        public final SqlColumn<Date> glueUpdatetime = column("glue_updatetime", JDBCType.TIMESTAMP);

        public final SqlColumn<String> childJobid = column("child_jobid", JDBCType.VARCHAR);

        public final SqlColumn<Byte> triggerStatus = column("trigger_status", JDBCType.TINYINT);

        public final SqlColumn<Long> triggerLastTime = column("trigger_last_time", JDBCType.BIGINT);

        public final SqlColumn<Long> triggerNextTime = column("trigger_next_time", JDBCType.BIGINT);

        public final SqlColumn<String> glueSource = column("glue_source", JDBCType.LONGVARCHAR);

        public SnailJobInfo() {
            super("snail_job_info");
        }
    }
}