package com.snailwu.job.admin.mapper;

import com.snailwu.job.admin.core.model.JobLog;
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

import static com.snailwu.job.admin.mapper.JobLogDynamicSqlSupport.*;
import static org.mybatis.dynamic.sql.SqlBuilder.isEqualTo;

@Mapper
public interface JobLogMapper {
    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    BasicColumn[] selectList = BasicColumn.columnList(id, jobId, groupId, executorAddress, executorHandler, executorParam, executorFailRetryCount, triggerTime, triggerCode, execTime, execCode, alarmStatus, triggerMsg, execLog);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    @SelectProvider(type = SqlProviderAdapter.class, method = "select")
    long count(SelectStatementProvider selectStatement);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    @DeleteProvider(type=SqlProviderAdapter.class, method="delete")
    int delete(DeleteStatementProvider deleteStatement);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    @InsertProvider(type=SqlProviderAdapter.class, method="insert")
    int insert(InsertStatementProvider<JobLog> insertStatement);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    @InsertProvider(type=SqlProviderAdapter.class, method="insertMultiple")
    int insertMultiple(MultiRowInsertStatementProvider<JobLog> multipleInsertStatement);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    @ResultMap("JobLogResult")
    Optional<JobLog> selectOne(SelectStatementProvider selectStatement);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    @Results(id="JobLogResult", value = {
            @Result(column = "id", property = "id", jdbcType = JdbcType.BIGINT, id = true),
            @Result(column = "job_id", property = "jobId", jdbcType = JdbcType.INTEGER),
            @Result(column = "group_id", property = "groupId", jdbcType = JdbcType.INTEGER),
            @Result(column = "executor_address", property = "executorAddress", jdbcType = JdbcType.VARCHAR),
            @Result(column = "executor_handler", property = "executorHandler", jdbcType = JdbcType.VARCHAR),
            @Result(column = "executor_param", property = "executorParam", jdbcType = JdbcType.VARCHAR),
            @Result(column = "executor_fail_retry_count", property = "executorFailRetryCount", jdbcType = JdbcType.TINYINT),
            @Result(column = "trigger_time", property = "triggerTime", jdbcType = JdbcType.TIMESTAMP),
            @Result(column = "trigger_code", property = "triggerCode", jdbcType = JdbcType.INTEGER),
            @Result(column = "exec_time", property = "execTime", jdbcType = JdbcType.TIMESTAMP),
            @Result(column = "exec_code", property = "execCode", jdbcType = JdbcType.INTEGER),
            @Result(column = "alarm_status", property = "alarmStatus", jdbcType = JdbcType.TINYINT),
            @Result(column = "trigger_msg", property = "triggerMsg", jdbcType = JdbcType.LONGVARCHAR),
            @Result(column = "exec_log", property = "execLog", jdbcType = JdbcType.LONGVARCHAR)
    })
    List<JobLog> selectMany(SelectStatementProvider selectStatement);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    @UpdateProvider(type=SqlProviderAdapter.class, method="update")
    int update(UpdateStatementProvider updateStatement);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default long count(CountDSLCompleter completer) {
        return MyBatis3Utils.countFrom(this::count, jobLog, completer);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int delete(DeleteDSLCompleter completer) {
        return MyBatis3Utils.deleteFrom(this::delete, jobLog, completer);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int deleteByPrimaryKey(Long id_) {
        return delete(c -> 
            c.where(id, isEqualTo(id_))
        );
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int insert(JobLog record) {
        return MyBatis3Utils.insert(this::insert, record, jobLog, c ->
                c.map(id).toProperty("id")
                        .map(jobId).toProperty("jobId")
                        .map(groupId).toProperty("groupId")
                        .map(executorAddress).toProperty("executorAddress")
            .map(executorHandler).toProperty("executorHandler")
            .map(executorParam).toProperty("executorParam")
            .map(executorFailRetryCount).toProperty("executorFailRetryCount")
            .map(triggerTime).toProperty("triggerTime")
            .map(triggerCode).toProperty("triggerCode")
            .map(execTime).toProperty("execTime")
            .map(execCode).toProperty("execCode")
            .map(alarmStatus).toProperty("alarmStatus")
            .map(triggerMsg).toProperty("triggerMsg")
            .map(execLog).toProperty("execLog")
        );
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int insertMultiple(Collection<JobLog> records) {
        return MyBatis3Utils.insertMultiple(this::insertMultiple, records, jobLog, c ->
                c.map(id).toProperty("id")
                        .map(jobId).toProperty("jobId")
                        .map(groupId).toProperty("groupId")
                        .map(executorAddress).toProperty("executorAddress")
            .map(executorHandler).toProperty("executorHandler")
            .map(executorParam).toProperty("executorParam")
            .map(executorFailRetryCount).toProperty("executorFailRetryCount")
            .map(triggerTime).toProperty("triggerTime")
            .map(triggerCode).toProperty("triggerCode")
            .map(execTime).toProperty("execTime")
            .map(execCode).toProperty("execCode")
            .map(alarmStatus).toProperty("alarmStatus")
            .map(triggerMsg).toProperty("triggerMsg")
            .map(execLog).toProperty("execLog")
        );
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int insertSelective(JobLog record) {
        return MyBatis3Utils.insert(this::insert, record, jobLog, c ->
                c.map(id).toPropertyWhenPresent("id", record::getId)
                        .map(jobId).toPropertyWhenPresent("jobId", record::getJobId)
                        .map(groupId).toPropertyWhenPresent("groupId", record::getGroupId)
                        .map(executorAddress).toPropertyWhenPresent("executorAddress", record::getExecutorAddress)
            .map(executorHandler).toPropertyWhenPresent("executorHandler", record::getExecutorHandler)
            .map(executorParam).toPropertyWhenPresent("executorParam", record::getExecutorParam)
            .map(executorFailRetryCount).toPropertyWhenPresent("executorFailRetryCount", record::getExecutorFailRetryCount)
            .map(triggerTime).toPropertyWhenPresent("triggerTime", record::getTriggerTime)
            .map(triggerCode).toPropertyWhenPresent("triggerCode", record::getTriggerCode)
            .map(execTime).toPropertyWhenPresent("execTime", record::getExecTime)
            .map(execCode).toPropertyWhenPresent("execCode", record::getExecCode)
            .map(alarmStatus).toPropertyWhenPresent("alarmStatus", record::getAlarmStatus)
            .map(triggerMsg).toPropertyWhenPresent("triggerMsg", record::getTriggerMsg)
            .map(execLog).toPropertyWhenPresent("execLog", record::getExecLog)
        );
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default Optional<JobLog> selectOne(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectOne(this::selectOne, selectList, jobLog, completer);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default List<JobLog> select(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectList(this::selectMany, selectList, jobLog, completer);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default List<JobLog> selectDistinct(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectDistinct(this::selectMany, selectList, jobLog, completer);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default Optional<JobLog> selectByPrimaryKey(Long id_) {
        return selectOne(c ->
            c.where(id, isEqualTo(id_))
        );
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int update(UpdateDSLCompleter completer) {
        return MyBatis3Utils.update(this::update, jobLog, completer);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    static UpdateDSL<UpdateModel> updateAllColumns(JobLog record, UpdateDSL<UpdateModel> dsl) {
        return dsl.set(id).equalTo(record::getId)
                .set(jobId).equalTo(record::getJobId)
                .set(groupId).equalTo(record::getGroupId)
                .set(executorAddress).equalTo(record::getExecutorAddress)
                .set(executorHandler).equalTo(record::getExecutorHandler)
                .set(executorParam).equalTo(record::getExecutorParam)
                .set(executorFailRetryCount).equalTo(record::getExecutorFailRetryCount)
                .set(triggerTime).equalTo(record::getTriggerTime)
                .set(triggerCode).equalTo(record::getTriggerCode)
                .set(execTime).equalTo(record::getExecTime)
                .set(execCode).equalTo(record::getExecCode)
                .set(alarmStatus).equalTo(record::getAlarmStatus)
                .set(triggerMsg).equalTo(record::getTriggerMsg)
                .set(execLog).equalTo(record::getExecLog);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    static UpdateDSL<UpdateModel> updateSelectiveColumns(JobLog record, UpdateDSL<UpdateModel> dsl) {
        return dsl.set(id).equalToWhenPresent(record::getId)
                .set(jobId).equalToWhenPresent(record::getJobId)
                .set(groupId).equalToWhenPresent(record::getGroupId)
                .set(executorAddress).equalToWhenPresent(record::getExecutorAddress)
                .set(executorHandler).equalToWhenPresent(record::getExecutorHandler)
                .set(executorParam).equalToWhenPresent(record::getExecutorParam)
                .set(executorFailRetryCount).equalToWhenPresent(record::getExecutorFailRetryCount)
                .set(triggerTime).equalToWhenPresent(record::getTriggerTime)
                .set(triggerCode).equalToWhenPresent(record::getTriggerCode)
                .set(execTime).equalToWhenPresent(record::getExecTime)
                .set(execCode).equalToWhenPresent(record::getExecCode)
                .set(alarmStatus).equalToWhenPresent(record::getAlarmStatus)
                .set(triggerMsg).equalToWhenPresent(record::getTriggerMsg)
                .set(execLog).equalToWhenPresent(record::getExecLog);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int updateByPrimaryKey(JobLog record) {
        return update(c ->
                c.set(jobId).equalTo(record::getJobId)
                        .set(groupId).equalTo(record::getGroupId)
                        .set(executorAddress).equalTo(record::getExecutorAddress)
                        .set(executorHandler).equalTo(record::getExecutorHandler)
                        .set(executorParam).equalTo(record::getExecutorParam)
                        .set(executorFailRetryCount).equalTo(record::getExecutorFailRetryCount)
                        .set(triggerTime).equalTo(record::getTriggerTime)
                        .set(triggerCode).equalTo(record::getTriggerCode)
                        .set(execTime).equalTo(record::getExecTime)
                        .set(execCode).equalTo(record::getExecCode)
                        .set(alarmStatus).equalTo(record::getAlarmStatus)
                        .set(triggerMsg).equalTo(record::getTriggerMsg)
                        .set(execLog).equalTo(record::getExecLog)
            .where(id, isEqualTo(record::getId))
        );
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int updateByPrimaryKeySelective(JobLog record) {
        return update(c ->
                c.set(jobId).equalToWhenPresent(record::getJobId)
                        .set(groupId).equalToWhenPresent(record::getGroupId)
                        .set(executorAddress).equalToWhenPresent(record::getExecutorAddress)
                        .set(executorHandler).equalToWhenPresent(record::getExecutorHandler)
                        .set(executorParam).equalToWhenPresent(record::getExecutorParam)
                        .set(executorFailRetryCount).equalToWhenPresent(record::getExecutorFailRetryCount)
                        .set(triggerTime).equalToWhenPresent(record::getTriggerTime)
                        .set(triggerCode).equalToWhenPresent(record::getTriggerCode)
                        .set(execTime).equalToWhenPresent(record::getExecTime)
                        .set(execCode).equalToWhenPresent(record::getExecCode)
                        .set(alarmStatus).equalToWhenPresent(record::getAlarmStatus)
                        .set(triggerMsg).equalToWhenPresent(record::getTriggerMsg)
                        .set(execLog).equalToWhenPresent(record::getExecLog)
            .where(id, isEqualTo(record::getId))
        );
    }
}