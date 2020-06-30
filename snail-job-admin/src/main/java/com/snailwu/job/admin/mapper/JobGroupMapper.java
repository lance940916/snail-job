package com.snailwu.job.admin.mapper;

import com.snailwu.job.admin.core.model.JobGroup;
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

import static com.snailwu.job.admin.mapper.JobGroupDynamicSqlSupport.*;
import static org.mybatis.dynamic.sql.SqlBuilder.isEqualTo;

@Mapper
public interface JobGroupMapper {
    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    BasicColumn[] selectList = BasicColumn.columnList(id, name, uuid, addressList);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    @SelectProvider(type = SqlProviderAdapter.class, method = "select")
    long count(SelectStatementProvider selectStatement);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    @DeleteProvider(type = SqlProviderAdapter.class, method = "delete")
    int delete(DeleteStatementProvider deleteStatement);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    @InsertProvider(type = SqlProviderAdapter.class, method = "insert")
    int insert(InsertStatementProvider<JobGroup> insertStatement);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    @InsertProvider(type = SqlProviderAdapter.class, method = "insertMultiple")
    int insertMultiple(MultiRowInsertStatementProvider<JobGroup> multipleInsertStatement);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    @SelectProvider(type = SqlProviderAdapter.class, method = "select")
    @ResultMap("JobGroupResult")
    Optional<JobGroup> selectOne(SelectStatementProvider selectStatement);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    @SelectProvider(type = SqlProviderAdapter.class, method = "select")
    @Results(id = "JobGroupResult", value = {
            @Result(column = "id", property = "id", jdbcType = JdbcType.INTEGER, id = true),
            @Result(column = "name", property = "name", jdbcType = JdbcType.VARCHAR),
            @Result(column = "uuid", property = "uuid", jdbcType = JdbcType.VARCHAR),
            @Result(column = "address_list", property = "addressList", jdbcType = JdbcType.VARCHAR)
    })
    List<JobGroup> selectMany(SelectStatementProvider selectStatement);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    @UpdateProvider(type=SqlProviderAdapter.class, method="update")
    int update(UpdateStatementProvider updateStatement);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default long count(CountDSLCompleter completer) {
        return MyBatis3Utils.countFrom(this::count, jobGroup, completer);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int delete(DeleteDSLCompleter completer) {
        return MyBatis3Utils.deleteFrom(this::delete, jobGroup, completer);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int deleteByPrimaryKey(Integer id_) {
        return delete(c -> 
            c.where(id, isEqualTo(id_))
        );
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int insert(JobGroup record) {
        return MyBatis3Utils.insert(this::insert, record, jobGroup, c ->
                c.map(id).toProperty("id")
                        .map(name).toProperty("name")
                        .map(uuid).toProperty("uuid")
                        .map(addressList).toProperty("addressList")
        );
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int insertMultiple(Collection<JobGroup> records) {
        return MyBatis3Utils.insertMultiple(this::insertMultiple, records, jobGroup, c ->
                c.map(id).toProperty("id")
                        .map(name).toProperty("name")
                        .map(uuid).toProperty("uuid")
                        .map(addressList).toProperty("addressList")
        );
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int insertSelective(JobGroup record) {
        return MyBatis3Utils.insert(this::insert, record, jobGroup, c ->
                c.map(id).toPropertyWhenPresent("id", record::getId)
                        .map(name).toPropertyWhenPresent("name", record::getName)
                        .map(uuid).toPropertyWhenPresent("uuid", record::getUuid)
                        .map(addressList).toPropertyWhenPresent("addressList", record::getAddressList)
        );
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default Optional<JobGroup> selectOne(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectOne(this::selectOne, selectList, jobGroup, completer);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default List<JobGroup> select(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectList(this::selectMany, selectList, jobGroup, completer);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default List<JobGroup> selectDistinct(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectDistinct(this::selectMany, selectList, jobGroup, completer);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default Optional<JobGroup> selectByPrimaryKey(Integer id_) {
        return selectOne(c ->
                c.where(id, isEqualTo(id_))
        );
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int update(UpdateDSLCompleter completer) {
        return MyBatis3Utils.update(this::update, jobGroup, completer);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    static UpdateDSL<UpdateModel> updateAllColumns(JobGroup record, UpdateDSL<UpdateModel> dsl) {
        return dsl.set(id).equalTo(record::getId)
                .set(name).equalTo(record::getName)
                .set(uuid).equalTo(record::getUuid)
                .set(addressList).equalTo(record::getAddressList);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    static UpdateDSL<UpdateModel> updateSelectiveColumns(JobGroup record, UpdateDSL<UpdateModel> dsl) {
        return dsl.set(id).equalToWhenPresent(record::getId)
                .set(name).equalToWhenPresent(record::getName)
                .set(uuid).equalToWhenPresent(record::getUuid)
                .set(addressList).equalToWhenPresent(record::getAddressList);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int updateByPrimaryKey(JobGroup record) {
        return update(c ->
                c.set(name).equalTo(record::getName)
                        .set(uuid).equalTo(record::getUuid)
                        .set(addressList).equalTo(record::getAddressList)
                        .where(id, isEqualTo(record::getId))
        );
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int updateByPrimaryKeySelective(JobGroup record) {
        return update(c ->
                c.set(name).equalToWhenPresent(record::getName)
                        .set(uuid).equalToWhenPresent(record::getUuid)
                        .set(addressList).equalToWhenPresent(record::getAddressList)
                        .where(id, isEqualTo(record::getId))
        );
    }
}