package com.snailwu.job.admin.mapper;

import java.sql.JDBCType;
import java.util.Date;
import javax.annotation.Generated;
import org.mybatis.dynamic.sql.SqlColumn;
import org.mybatis.dynamic.sql.SqlTable;

public final class JobLogReportDynamicSqlSupport {
    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final JobLogReport jobLogReport = new JobLogReport();

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<Integer> id = jobLogReport.id;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<Date> triggerTime = jobLogReport.triggerTime;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<Integer> runningCount = jobLogReport.runningCount;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<Integer> successCount = jobLogReport.successCount;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<Integer> failCount = jobLogReport.failCount;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final class JobLogReport extends SqlTable {
        public final SqlColumn<Integer> id = column("id", JDBCType.INTEGER);

        public final SqlColumn<Date> triggerTime = column("trigger_time", JDBCType.TIMESTAMP);

        public final SqlColumn<Integer> runningCount = column("running_count", JDBCType.INTEGER);

        public final SqlColumn<Integer> successCount = column("success_count", JDBCType.INTEGER);

        public final SqlColumn<Integer> failCount = column("fail_count", JDBCType.INTEGER);

        public JobLogReport() {
            super("job_log_report");
        }
    }
}