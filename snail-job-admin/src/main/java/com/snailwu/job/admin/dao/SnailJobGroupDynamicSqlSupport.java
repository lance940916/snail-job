package com.snailwu.job.admin.dao;

import java.sql.JDBCType;
import javax.annotation.Generated;
import org.mybatis.dynamic.sql.SqlColumn;
import org.mybatis.dynamic.sql.SqlTable;

public final class SnailJobGroupDynamicSqlSupport {
    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SnailJobGroup snailJobGroup = new SnailJobGroup();

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<Integer> id = snailJobGroup.id;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<String> appName = snailJobGroup.appName;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<String> title = snailJobGroup.title;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<Byte> addressType = snailJobGroup.addressType;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<String> addressList = snailJobGroup.addressList;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final class SnailJobGroup extends SqlTable {
        public final SqlColumn<Integer> id = column("id", JDBCType.INTEGER);

        public final SqlColumn<String> appName = column("app_name", JDBCType.VARCHAR);

        public final SqlColumn<String> title = column("title", JDBCType.VARCHAR);

        public final SqlColumn<Byte> addressType = column("address_type", JDBCType.TINYINT);

        public final SqlColumn<String> addressList = column("address_list", JDBCType.VARCHAR);

        public SnailJobGroup() {
            super("snail_job_group");
        }
    }
}