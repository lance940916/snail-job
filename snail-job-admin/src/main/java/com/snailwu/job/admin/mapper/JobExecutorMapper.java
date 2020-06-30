package com.snailwu.job.admin.mapper;

import com.snailwu.job.admin.core.model.JobExecutor;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;
import org.mybatis.dynamic.sql.BasicColumn;
import org.mybatis.dynamic.sql.delete.DeleteDSLCompleter;
import org.mybatis.dynamic.sql.delete.render.DeleteStatementProvider;
import org.mybatis.dynamic.sql.insert.render.InsertStatementProvider;
import org.mybatis.dynamic.sql.insert.render.MultiRowInsertStatementProvider;
import org.mybatis.dynamic.sql.select.CountDSLCompleter;
import org.mybatis.dynamic.sql.select.SelectDSLCompleter;
import org.mybatis.dynamic.sql.select.render.SelectStatementProvider;
import org.mybatis.dynamic.sql.update.UpdateDSL;
import org.mybatis.dynamic.sql.update.UpdateDSLCompleter;
import org.mybatis.dynamic.sql.update.UpdateModel;
import org.mybatis.dynamic.sql.update.render.UpdateStatementProvider;
import org.mybatis.dynamic.sql.util.SqlProviderAdapter;
import org.mybatis.dynamic.sql.util.mybatis3.MyBatis3Utils;

import javax.annotation.Generated;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static com.snailwu.job.admin.mapper.JobExecutorDynamicSqlSupport.*;
import static org.mybatis.dynamic.sql.SqlBuilder.isEqualTo;

@Mapper
public interface JobExecutorMapper {
    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    BasicColumn[] selectList = BasicColumn.columnList(id, groupUuid, address, registryType, updateTime);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    @SelectProvider(type = SqlProviderAdapter.class, method = "select")
    long count(SelectStatementProvider selectStatement);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    @DeleteProvider(type=SqlProviderAdapter.class, method="delete")
    int delete(DeleteStatementProvider deleteStatement);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    @InsertProvider(type=SqlProviderAdapter.class, method="insert")
    int insert(InsertStatementProvider<JobExecutor> insertStatement);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    @InsertProvider(type=SqlProviderAdapter.class, method="insertMultiple")
    int insertMultiple(MultiRowInsertStatementProvider<JobExecutor> multipleInsertStatement);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    @ResultMap("JobExecutorResult")
    Optional<JobExecutor> selectOne(SelectStatementProvider selectStatement);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    @Results(id="JobExecutorResult", value = {
            @Result(column = "id", property = "id", jdbcType = JdbcType.INTEGER, id = true),
            @Result(column = "group_uuid", property = "groupUuid", jdbcType = JdbcType.VARCHAR),
            @Result(column = "address", property = "address", jdbcType = JdbcType.VARCHAR),
            @Result(column = "registry_type", property = "registryType", jdbcType = JdbcType.CHAR),
            @Result(column = "update_time", property = "updateTime", jdbcType = JdbcType.TIMESTAMP)
    })
    List<JobExecutor> selectMany(SelectStatementProvider selectStatement);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    @UpdateProvider(type=SqlProviderAdapter.class, method="update")
    int update(UpdateStatementProvider updateStatement);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default long count(CountDSLCompleter completer) {
        return MyBatis3Utils.countFrom(this::count, jobExecutor, completer);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int delete(DeleteDSLCompleter completer) {
        return MyBatis3Utils.deleteFrom(this::delete, jobExecutor, completer);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int deleteByPrimaryKey(Integer id_) {
        return delete(c -> 
            c.where(id, isEqualTo(id_))
        );
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int insert(JobExecutor record) {
        return MyBatis3Utils.insert(this::insert, record, jobExecutor, c ->
                c.map(id).toProperty("id")
                        .map(groupUuid).toProperty("groupUuid")
                        .map(address).toProperty("address")
                        .map(registryType).toProperty("registryType")
                        .map(updateTime).toProperty("updateTime")
        );
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int insertMultiple(Collection<JobExecutor> records) {
        return MyBatis3Utils.insertMultiple(this::insertMultiple, records, jobExecutor, c ->
                c.map(id).toProperty("id")
                        .map(groupUuid).toProperty("groupUuid")
                        .map(address).toProperty("address")
                        .map(registryType).toProperty("registryType")
                        .map(updateTime).toProperty("updateTime")
        );
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int insertSelective(JobExecutor record) {
        return MyBatis3Utils.insert(this::insert, record, jobExecutor, c ->
                c.map(id).toPropertyWhenPresent("id", record::getId)
                        .map(groupUuid).toPropertyWhenPresent("groupUuid", record::getGroupUuid)
                        .map(address).toPropertyWhenPresent("address", record::getAddress)
                        .map(registryType).toPropertyWhenPresent("registryType", record::getRegistryType)
                        .map(updateTime).toPropertyWhenPresent("updateTime", record::getUpdateTime)
        );
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default Optional<JobExecutor> selectOne(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectOne(this::selectOne, selectList, jobExecutor, completer);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default List<JobExecutor> select(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectList(this::selectMany, selectList, jobExecutor, completer);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default List<JobExecutor> selectDistinct(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectDistinct(this::selectMany, selectList, jobExecutor, completer);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default Optional<JobExecutor> selectByPrimaryKey(Integer id_) {
        return selectOne(c ->
            c.where(id, isEqualTo(id_))
        );
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int update(UpdateDSLCompleter completer) {
        return MyBatis3Utils.update(this::update, jobExecutor, completer);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    static UpdateDSL<UpdateModel> updateAllColumns(JobExecutor record, UpdateDSL<UpdateModel> dsl) {
        return dsl.set(id).equalTo(record::getId)
                .set(groupUuid).equalTo(record::getGroupUuid)
                .set(address).equalTo(record::getAddress)
                .set(registryType).equalTo(record::getRegistryType)
                .set(updateTime).equalTo(record::getUpdateTime);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    static UpdateDSL<UpdateModel> updateSelectiveColumns(JobExecutor record, UpdateDSL<UpdateModel> dsl) {
        return dsl.set(id).equalToWhenPresent(record::getId)
                .set(groupUuid).equalToWhenPresent(record::getGroupUuid)
                .set(address).equalToWhenPresent(record::getAddress)
                .set(registryType).equalToWhenPresent(record::getRegistryType)
                .set(updateTime).equalToWhenPresent(record::getUpdateTime);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int updateByPrimaryKey(JobExecutor record) {
        return update(c ->
                c.set(groupUuid).equalTo(record::getGroupUuid)
                        .set(address).equalTo(record::getAddress)
                        .set(registryType).equalTo(record::getRegistryType)
                        .set(updateTime).equalTo(record::getUpdateTime)
                        .where(id, isEqualTo(record::getId))
        );
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int updateByPrimaryKeySelective(JobExecutor record) {
        return update(c ->
                c.set(groupUuid).equalToWhenPresent(record::getGroupUuid)
                        .set(address).equalToWhenPresent(record::getAddress)
                        .set(registryType).equalToWhenPresent(record::getRegistryType)
                        .set(updateTime).equalToWhenPresent(record::getUpdateTime)
                        .where(id, isEqualTo(record::getId))
        );
    }
}