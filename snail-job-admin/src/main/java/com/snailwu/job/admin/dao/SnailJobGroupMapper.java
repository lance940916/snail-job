package com.snailwu.job.admin.dao;

import static com.snailwu.job.admin.dao.SnailJobGroupDynamicSqlSupport.*;
import static org.mybatis.dynamic.sql.SqlBuilder.*;

import com.snailwu.job.admin.core.model.SnailJobGroup;
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
public interface SnailJobGroupMapper {
    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    BasicColumn[] selectList = BasicColumn.columnList(id, appName, title, addressType, addressList);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    long count(SelectStatementProvider selectStatement);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    @DeleteProvider(type=SqlProviderAdapter.class, method="delete")
    int delete(DeleteStatementProvider deleteStatement);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    @InsertProvider(type=SqlProviderAdapter.class, method="insert")
    int insert(InsertStatementProvider<SnailJobGroup> insertStatement);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    @InsertProvider(type=SqlProviderAdapter.class, method="insertMultiple")
    int insertMultiple(MultiRowInsertStatementProvider<SnailJobGroup> multipleInsertStatement);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    @ResultMap("SnailJobGroupResult")
    Optional<SnailJobGroup> selectOne(SelectStatementProvider selectStatement);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    @Results(id="SnailJobGroupResult", value = {
        @Result(column="id", property="id", jdbcType=JdbcType.INTEGER, id=true),
        @Result(column="app_name", property="appName", jdbcType=JdbcType.VARCHAR),
        @Result(column="title", property="title", jdbcType=JdbcType.VARCHAR),
        @Result(column="address_type", property="addressType", jdbcType=JdbcType.TINYINT),
        @Result(column="address_list", property="addressList", jdbcType=JdbcType.VARCHAR)
    })
    List<SnailJobGroup> selectMany(SelectStatementProvider selectStatement);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    @UpdateProvider(type=SqlProviderAdapter.class, method="update")
    int update(UpdateStatementProvider updateStatement);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default long count(CountDSLCompleter completer) {
        return MyBatis3Utils.countFrom(this::count, snailJobGroup, completer);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int delete(DeleteDSLCompleter completer) {
        return MyBatis3Utils.deleteFrom(this::delete, snailJobGroup, completer);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int deleteByPrimaryKey(Integer id_) {
        return delete(c -> 
            c.where(id, isEqualTo(id_))
        );
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int insert(SnailJobGroup record) {
        return MyBatis3Utils.insert(this::insert, record, snailJobGroup, c ->
            c.map(id).toProperty("id")
            .map(appName).toProperty("appName")
            .map(title).toProperty("title")
            .map(addressType).toProperty("addressType")
            .map(addressList).toProperty("addressList")
        );
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int insertMultiple(Collection<SnailJobGroup> records) {
        return MyBatis3Utils.insertMultiple(this::insertMultiple, records, snailJobGroup, c ->
            c.map(id).toProperty("id")
            .map(appName).toProperty("appName")
            .map(title).toProperty("title")
            .map(addressType).toProperty("addressType")
            .map(addressList).toProperty("addressList")
        );
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int insertSelective(SnailJobGroup record) {
        return MyBatis3Utils.insert(this::insert, record, snailJobGroup, c ->
            c.map(id).toPropertyWhenPresent("id", record::getId)
            .map(appName).toPropertyWhenPresent("appName", record::getAppName)
            .map(title).toPropertyWhenPresent("title", record::getTitle)
            .map(addressType).toPropertyWhenPresent("addressType", record::getAddressType)
            .map(addressList).toPropertyWhenPresent("addressList", record::getAddressList)
        );
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default Optional<SnailJobGroup> selectOne(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectOne(this::selectOne, selectList, snailJobGroup, completer);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default List<SnailJobGroup> select(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectList(this::selectMany, selectList, snailJobGroup, completer);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default List<SnailJobGroup> selectDistinct(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectDistinct(this::selectMany, selectList, snailJobGroup, completer);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default Optional<SnailJobGroup> selectByPrimaryKey(Integer id_) {
        return selectOne(c ->
            c.where(id, isEqualTo(id_))
        );
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int update(UpdateDSLCompleter completer) {
        return MyBatis3Utils.update(this::update, snailJobGroup, completer);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    static UpdateDSL<UpdateModel> updateAllColumns(SnailJobGroup record, UpdateDSL<UpdateModel> dsl) {
        return dsl.set(id).equalTo(record::getId)
                .set(appName).equalTo(record::getAppName)
                .set(title).equalTo(record::getTitle)
                .set(addressType).equalTo(record::getAddressType)
                .set(addressList).equalTo(record::getAddressList);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    static UpdateDSL<UpdateModel> updateSelectiveColumns(SnailJobGroup record, UpdateDSL<UpdateModel> dsl) {
        return dsl.set(id).equalToWhenPresent(record::getId)
                .set(appName).equalToWhenPresent(record::getAppName)
                .set(title).equalToWhenPresent(record::getTitle)
                .set(addressType).equalToWhenPresent(record::getAddressType)
                .set(addressList).equalToWhenPresent(record::getAddressList);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int updateByPrimaryKey(SnailJobGroup record) {
        return update(c ->
            c.set(appName).equalTo(record::getAppName)
            .set(title).equalTo(record::getTitle)
            .set(addressType).equalTo(record::getAddressType)
            .set(addressList).equalTo(record::getAddressList)
            .where(id, isEqualTo(record::getId))
        );
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int updateByPrimaryKeySelective(SnailJobGroup record) {
        return update(c ->
            c.set(appName).equalToWhenPresent(record::getAppName)
            .set(title).equalToWhenPresent(record::getTitle)
            .set(addressType).equalToWhenPresent(record::getAddressType)
            .set(addressList).equalToWhenPresent(record::getAddressList)
            .where(id, isEqualTo(record::getId))
        );
    }
}