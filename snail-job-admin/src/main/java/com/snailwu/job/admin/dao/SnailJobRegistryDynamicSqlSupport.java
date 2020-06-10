package com.snailwu.job.admin.dao;

import java.sql.JDBCType;
import java.util.Date;
import javax.annotation.Generated;
import org.mybatis.dynamic.sql.SqlColumn;
import org.mybatis.dynamic.sql.SqlTable;

public final class SnailJobRegistryDynamicSqlSupport {
    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SnailJobRegistry snailJobRegistry = new SnailJobRegistry();

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<Integer> id = snailJobRegistry.id;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<String> registryGroup = snailJobRegistry.registryGroup;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<String> registryKey = snailJobRegistry.registryKey;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<String> registryValue = snailJobRegistry.registryValue;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<Date> updateTime = snailJobRegistry.updateTime;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final class SnailJobRegistry extends SqlTable {
        public final SqlColumn<Integer> id = column("id", JDBCType.INTEGER);

        public final SqlColumn<String> registryGroup = column("registry_group", JDBCType.VARCHAR);

        public final SqlColumn<String> registryKey = column("registry_key", JDBCType.VARCHAR);

        public final SqlColumn<String> registryValue = column("registry_value", JDBCType.VARCHAR);

        public final SqlColumn<Date> updateTime = column("update_time", JDBCType.TIMESTAMP);

        public SnailJobRegistry() {
            super("snail_job_registry");
        }
    }
}