package com.snailwu.job.admin.dao;

import static com.snailwu.job.admin.dao.SnailJobRegistryDynamicSqlSupport.*;
import static org.mybatis.dynamic.sql.SqlBuilder.*;

import com.snailwu.job.admin.core.model.SnailJobRegistry;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import javax.annotation.Generated;
import org.apache.ibatis.annotations.DeleteProvider;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;
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

@Mapper
public interface SnailJobRegistryMapper {
    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    BasicColumn[] selectList = BasicColumn.columnList(id, registryGroup, registryKey, registryValue, updateTime);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    long count(SelectStatementProvider selectStatement);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    @DeleteProvider(type=SqlProviderAdapter.class, method="delete")
    int delete(DeleteStatementProvider deleteStatement);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    @InsertProvider(type=SqlProviderAdapter.class, method="insert")
    int insert(InsertStatementProvider<SnailJobRegistry> insertStatement);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    @InsertProvider(type=SqlProviderAdapter.class, method="insertMultiple")
    int insertMultiple(MultiRowInsertStatementProvider<SnailJobRegistry> multipleInsertStatement);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    @ResultMap("SnailJobRegistryResult")
    Optional<SnailJobRegistry> selectOne(SelectStatementProvider selectStatement);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    @Results(id="SnailJobRegistryResult", value = {
        @Result(column="id", property="id", jdbcType=JdbcType.INTEGER, id=true),
        @Result(column="registry_group", property="registryGroup", jdbcType=JdbcType.VARCHAR),
        @Result(column="registry_key", property="registryKey", jdbcType=JdbcType.VARCHAR),
        @Result(column="registry_value", property="registryValue", jdbcType=JdbcType.VARCHAR),
        @Result(column="update_time", property="updateTime", jdbcType=JdbcType.TIMESTAMP)
    })
    List<SnailJobRegistry> selectMany(SelectStatementProvider selectStatement);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    @UpdateProvider(type=SqlProviderAdapter.class, method="update")
    int update(UpdateStatementProvider updateStatement);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default long count(CountDSLCompleter completer) {
        return MyBatis3Utils.countFrom(this::count, snailJobRegistry, completer);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int delete(DeleteDSLCompleter completer) {
        return MyBatis3Utils.deleteFrom(this::delete, snailJobRegistry, completer);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int deleteByPrimaryKey(Integer id_) {
        return delete(c -> 
            c.where(id, isEqualTo(id_))
        );
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int insert(SnailJobRegistry record) {
        return MyBatis3Utils.insert(this::insert, record, snailJobRegistry, c ->
            c.map(id).toProperty("id")
            .map(registryGroup).toProperty("registryGroup")
            .map(registryKey).toProperty("registryKey")
            .map(registryValue).toProperty("registryValue")
            .map(updateTime).toProperty("updateTime")
        );
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int insertMultiple(Collection<SnailJobRegistry> records) {
        return MyBatis3Utils.insertMultiple(this::insertMultiple, records, snailJobRegistry, c ->
            c.map(id).toProperty("id")
            .map(registryGroup).toProperty("registryGroup")
            .map(registryKey).toProperty("registryKey")
            .map(registryValue).toProperty("registryValue")
            .map(updateTime).toProperty("updateTime")
        );
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int insertSelective(SnailJobRegistry record) {
        return MyBatis3Utils.insert(this::insert, record, snailJobRegistry, c ->
            c.map(id).toPropertyWhenPresent("id", record::getId)
            .map(registryGroup).toPropertyWhenPresent("registryGroup", record::getRegistryGroup)
            .map(registryKey).toPropertyWhenPresent("registryKey", record::getRegistryKey)
            .map(registryValue).toPropertyWhenPresent("registryValue", record::getRegistryValue)
            .map(updateTime).toPropertyWhenPresent("updateTime", record::getUpdateTime)
        );
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default Optional<SnailJobRegistry> selectOne(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectOne(this::selectOne, selectList, snailJobRegistry, completer);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default List<SnailJobRegistry> select(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectList(this::selectMany, selectList, snailJobRegistry, completer);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default List<SnailJobRegistry> selectDistinct(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectDistinct(this::selectMany, selectList, snailJobRegistry, completer);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default Optional<SnailJobRegistry> selectByPrimaryKey(Integer id_) {
        return selectOne(c ->
            c.where(id, isEqualTo(id_))
        );
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int update(UpdateDSLCompleter completer) {
        return MyBatis3Utils.update(this::update, snailJobRegistry, completer);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    static UpdateDSL<UpdateModel> updateAllColumns(SnailJobRegistry record, UpdateDSL<UpdateModel> dsl) {
        return dsl.set(id).equalTo(record::getId)
                .set(registryGroup).equalTo(record::getRegistryGroup)
                .set(registryKey).equalTo(record::getRegistryKey)
                .set(registryValue).equalTo(record::getRegistryValue)
                .set(updateTime).equalTo(record::getUpdateTime);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    static UpdateDSL<UpdateModel> updateSelectiveColumns(SnailJobRegistry record, UpdateDSL<UpdateModel> dsl) {
        return dsl.set(id).equalToWhenPresent(record::getId)
                .set(registryGroup).equalToWhenPresent(record::getRegistryGroup)
                .set(registryKey).equalToWhenPresent(record::getRegistryKey)
                .set(registryValue).equalToWhenPresent(record::getRegistryValue)
                .set(updateTime).equalToWhenPresent(record::getUpdateTime);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int updateByPrimaryKey(SnailJobRegistry record) {
        return update(c ->
            c.set(registryGroup).equalTo(record::getRegistryGroup)
            .set(registryKey).equalTo(record::getRegistryKey)
            .set(registryValue).equalTo(record::getRegistryValue)
            .set(updateTime).equalTo(record::getUpdateTime)
            .where(id, isEqualTo(record::getId))
        );
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int updateByPrimaryKeySelective(SnailJobRegistry record) {
        return update(c ->
            c.set(registryGroup).equalToWhenPresent(record::getRegistryGroup)
            .set(registryKey).equalToWhenPresent(record::getRegistryKey)
            .set(registryValue).equalToWhenPresent(record::getRegistryValue)
            .set(updateTime).equalToWhenPresent(record::getUpdateTime)
            .where(id, isEqualTo(record::getId))
        );
    }
}