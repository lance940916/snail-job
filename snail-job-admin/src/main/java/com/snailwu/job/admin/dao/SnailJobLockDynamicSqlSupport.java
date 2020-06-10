package com.snailwu.job.admin.dao;

import java.sql.JDBCType;
import javax.annotation.Generated;
import org.mybatis.dynamic.sql.SqlColumn;
import org.mybatis.dynamic.sql.SqlTable;

public final class SnailJobLockDynamicSqlSupport {
    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SnailJobLock snailJobLock = new SnailJobLock();

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<String> lockName = snailJobLock.lockName;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final class SnailJobLock extends SqlTable {
        public final SqlColumn<String> lockName = column("lock_name", JDBCType.VARCHAR);

        public SnailJobLock() {
            super("snail_job_lock");
        }
    }
}