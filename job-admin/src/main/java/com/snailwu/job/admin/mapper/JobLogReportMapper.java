package com.snailwu.job.admin.mapper;

import com.snailwu.job.admin.model.JobLogReport;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;
import org.mybatis.dynamic.sql.BasicColumn;
import org.mybatis.dynamic.sql.delete.DeleteDSLCompleter;
import org.mybatis.dynamic.sql.delete.render.DeleteStatementProvider;
import org.mybatis.dynamic.sql.insert.render.InsertStatementProvider;
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
import java.util.List;
import java.util.Optional;

import static com.snailwu.job.admin.mapper.JobLogReportDynamicSqlSupport.*;
import static org.mybatis.dynamic.sql.SqlBuilder.isEqualTo;

@Mapper
public interface JobLogReportMapper {
    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    BasicColumn[] selectList = BasicColumn.columnList(id, triggerDay, runningCount, successCount, failCount);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    long count(SelectStatementProvider selectStatement);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    @DeleteProvider(type=SqlProviderAdapter.class, method="delete")
    int delete(DeleteStatementProvider deleteStatement);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    @InsertProvider(type=SqlProviderAdapter.class, method="insert")
    @SelectKey(statement="SELECT LAST_INSERT_ID()", keyProperty="record.id", before=false, resultType=Integer.class)
    int insert(InsertStatementProvider<JobLogReport> insertStatement);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    @ResultMap("JobLogReportResult")
    Optional<JobLogReport> selectOne(SelectStatementProvider selectStatement);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    @Results(id="JobLogReportResult", value = {
        @Result(column="id", property="id", jdbcType=JdbcType.INTEGER, id=true),
        @Result(column="trigger_day", property="triggerDay", jdbcType=JdbcType.DATE),
        @Result(column="running_count", property="runningCount", jdbcType=JdbcType.INTEGER),
        @Result(column="success_count", property="successCount", jdbcType=JdbcType.INTEGER),
        @Result(column="fail_count", property="failCount", jdbcType=JdbcType.INTEGER)
    })
    List<JobLogReport> selectMany(SelectStatementProvider selectStatement);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    @UpdateProvider(type=SqlProviderAdapter.class, method="update")
    int update(UpdateStatementProvider updateStatement);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default long count(CountDSLCompleter completer) {
        return MyBatis3Utils.countFrom(this::count, jobLogReport, completer);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int delete(DeleteDSLCompleter completer) {
        return MyBatis3Utils.deleteFrom(this::delete, jobLogReport, completer);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int deleteByPrimaryKey(Integer id_) {
        return delete(c -> 
            c.where(id, isEqualTo(id_))
        );
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int insert(JobLogReport record) {
        return MyBatis3Utils.insert(this::insert, record, jobLogReport, c ->
            c.map(triggerDay).toProperty("triggerDay")
            .map(runningCount).toProperty("runningCount")
            .map(successCount).toProperty("successCount")
            .map(failCount).toProperty("failCount")
        );
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int insertSelective(JobLogReport record) {
        return MyBatis3Utils.insert(this::insert, record, jobLogReport, c ->
            c.map(triggerDay).toPropertyWhenPresent("triggerDay", record::getTriggerDay)
            .map(runningCount).toPropertyWhenPresent("runningCount", record::getRunningCount)
            .map(successCount).toPropertyWhenPresent("successCount", record::getSuccessCount)
            .map(failCount).toPropertyWhenPresent("failCount", record::getFailCount)
        );
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default Optional<JobLogReport> selectOne(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectOne(this::selectOne, selectList, jobLogReport, completer);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default List<JobLogReport> select(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectList(this::selectMany, selectList, jobLogReport, completer);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default List<JobLogReport> selectDistinct(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectDistinct(this::selectMany, selectList, jobLogReport, completer);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default Optional<JobLogReport> selectByPrimaryKey(Integer id_) {
        return selectOne(c ->
            c.where(id, isEqualTo(id_))
        );
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int update(UpdateDSLCompleter completer) {
        return MyBatis3Utils.update(this::update, jobLogReport, completer);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    static UpdateDSL<UpdateModel> updateAllColumns(JobLogReport record, UpdateDSL<UpdateModel> dsl) {
        return dsl.set(triggerDay).equalTo(record::getTriggerDay)
                .set(runningCount).equalTo(record::getRunningCount)
                .set(successCount).equalTo(record::getSuccessCount)
                .set(failCount).equalTo(record::getFailCount);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    static UpdateDSL<UpdateModel> updateSelectiveColumns(JobLogReport record, UpdateDSL<UpdateModel> dsl) {
        return dsl.set(triggerDay).equalToWhenPresent(record::getTriggerDay)
                .set(runningCount).equalToWhenPresent(record::getRunningCount)
                .set(successCount).equalToWhenPresent(record::getSuccessCount)
                .set(failCount).equalToWhenPresent(record::getFailCount);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int updateByPrimaryKey(JobLogReport record) {
        return update(c ->
            c.set(triggerDay).equalTo(record::getTriggerDay)
            .set(runningCount).equalTo(record::getRunningCount)
            .set(successCount).equalTo(record::getSuccessCount)
            .set(failCount).equalTo(record::getFailCount)
            .where(id, isEqualTo(record::getId))
        );
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int updateByPrimaryKeySelective(JobLogReport record) {
        return update(c ->
            c.set(triggerDay).equalToWhenPresent(record::getTriggerDay)
            .set(runningCount).equalToWhenPresent(record::getRunningCount)
            .set(successCount).equalToWhenPresent(record::getSuccessCount)
            .set(failCount).equalToWhenPresent(record::getFailCount)
            .where(id, isEqualTo(record::getId))
        );
    }
}