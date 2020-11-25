package com.snailwu.job.admin.mapper;

import org.mybatis.dynamic.sql.SqlColumn;
import org.mybatis.dynamic.sql.SqlTable;

import javax.annotation.Generated;
import java.sql.JDBCType;
import java.util.Date;

public final class JobLogReportDynamicSqlSupport {
    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final JobLogReport jobLogReport = new JobLogReport();

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<Integer> id = jobLogReport.id;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<Date> triggerDay = jobLogReport.triggerDay;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<Integer> runningCount = jobLogReport.runningCount;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<Integer> successCount = jobLogReport.successCount;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<Integer> failCount = jobLogReport.failCount;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final class JobLogReport extends SqlTable {
        public final SqlColumn<Integer> id = column("id", JDBCType.INTEGER);

        public final SqlColumn<Date> triggerDay = column("trigger_day", JDBCType.DATE);

        public final SqlColumn<Integer> runningCount = column("running_count", JDBCType.INTEGER);

        public final SqlColumn<Integer> successCount = column("success_count", JDBCType.INTEGER);

        public final SqlColumn<Integer> failCount = column("fail_count", JDBCType.INTEGER);

        public JobLogReport() {
            super("job_log_report");
        }
    }
}