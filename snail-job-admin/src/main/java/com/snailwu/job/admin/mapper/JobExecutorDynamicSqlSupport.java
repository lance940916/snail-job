package com.snailwu.job.admin.mapper;

import org.mybatis.dynamic.sql.SqlColumn;
import org.mybatis.dynamic.sql.SqlTable;

import javax.annotation.Generated;
import java.sql.JDBCType;
import java.util.Date;

public final class JobExecutorDynamicSqlSupport {
    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final JobExecutor jobExecutor = new JobExecutor();

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<Integer> id = jobExecutor.id;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<String> groupUuid = jobExecutor.groupUuid;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<String> address = jobExecutor.address;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<String> registryType = jobExecutor.registryType;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<Date> updateTime = jobExecutor.updateTime;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final class JobExecutor extends SqlTable {
        public final SqlColumn<Integer> id = column("id", JDBCType.INTEGER);

        public final SqlColumn<String> groupUuid = column("group_uuid", JDBCType.VARCHAR);

        public final SqlColumn<String> address = column("address", JDBCType.VARCHAR);

        public final SqlColumn<String> registryType = column("registry_type", JDBCType.CHAR);

        public final SqlColumn<Date> updateTime = column("update_time", JDBCType.TIMESTAMP);

        public JobExecutor() {
            super("job_executor");
        }
    }
}