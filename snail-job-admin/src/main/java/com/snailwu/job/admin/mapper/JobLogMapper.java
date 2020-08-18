package com.snailwu.job.admin.mapper;

import com.snailwu.job.admin.controller.entity.JobLogVO;
import com.snailwu.job.admin.core.model.JobLog;
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

import static com.snailwu.job.admin.mapper.JobLogDynamicSqlSupport.*;
import static org.mybatis.dynamic.sql.SqlBuilder.isEqualTo;

@Mapper
public interface JobLogMapper {
    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    BasicColumn[] selectList = BasicColumn.columnList(id, jobId, groupName, executorAddress, executorHandler, executorParam, failRetryCount, triggerTime, triggerCode, triggerMsg, execTime, execCode, execMsg, alarmStatus);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    @SelectProvider(type = SqlProviderAdapter.class, method = "select")
    long count(SelectStatementProvider selectStatement);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    @DeleteProvider(type = SqlProviderAdapter.class, method = "delete")
    int delete(DeleteStatementProvider deleteStatement);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    @InsertProvider(type = SqlProviderAdapter.class, method = "insert")
    @SelectKey(statement = "SELECT LAST_INSERT_ID()", keyProperty = "record.id", before = false, resultType = Long.class)
    int insert(InsertStatementProvider<JobLog> insertStatement);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    @SelectProvider(type = SqlProviderAdapter.class, method = "select")
    @ResultMap("JobLogResult")
    Optional<JobLog> selectOne(SelectStatementProvider selectStatement);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    @SelectProvider(type = SqlProviderAdapter.class, method = "select")
    @Results(id = "JobLogResult", value = {
            @Result(column = "id", property = "id", jdbcType = JdbcType.BIGINT, id = true),
            @Result(column = "job_id", property = "jobId", jdbcType = JdbcType.INTEGER),
            @Result(column = "group_name", property = "groupName", jdbcType = JdbcType.VARCHAR),
            @Result(column = "executor_address", property = "executorAddress", jdbcType = JdbcType.VARCHAR),
            @Result(column = "executor_handler", property = "executorHandler", jdbcType = JdbcType.VARCHAR),
            @Result(column = "executor_param", property = "executorParam", jdbcType = JdbcType.VARCHAR),
            @Result(column = "fail_retry_count", property = "failRetryCount", jdbcType = JdbcType.TINYINT),
            @Result(column = "trigger_time", property = "triggerTime", jdbcType = JdbcType.TIMESTAMP),
            @Result(column = "trigger_code", property = "triggerCode", jdbcType = JdbcType.INTEGER),
            @Result(column = "trigger_msg", property = "triggerMsg", jdbcType = JdbcType.VARCHAR),
            @Result(column = "exec_time", property = "execTime", jdbcType = JdbcType.TIMESTAMP),
            @Result(column = "exec_code", property = "execCode", jdbcType = JdbcType.INTEGER),
            @Result(column = "exec_msg", property = "execMsg", jdbcType = JdbcType.VARCHAR),
            @Result(column = "alarm_status", property = "alarmStatus", jdbcType = JdbcType.TINYINT)
    })
    List<JobLog> selectMany(SelectStatementProvider selectStatement);

    @SelectProvider(type = SqlProviderAdapter.class, method = "select")
    @Results(id = "JobLogResultWithJobName", value = {
            @Result(column = "name", property = "jobName", jdbcType = JdbcType.VARCHAR),
    })
    @ResultMap({"JobLogResult", "JobLogResultWithJobName"})
    List<JobLogVO> selectManyWithJobName(SelectStatementProvider selectStatement);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    @UpdateProvider(type = SqlProviderAdapter.class, method = "update")
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
                c.map(jobId).toProperty("jobId")
                        .map(groupName).toProperty("groupName")
                        .map(executorAddress).toProperty("executorAddress")
                        .map(executorHandler).toProperty("executorHandler")
                        .map(executorParam).toProperty("executorParam")
                        .map(failRetryCount).toProperty("failRetryCount")
                        .map(triggerTime).toProperty("triggerTime")
                        .map(triggerCode).toProperty("triggerCode")
                        .map(triggerMsg).toProperty("triggerMsg")
                        .map(execTime).toProperty("execTime")
                        .map(execCode).toProperty("execCode")
                        .map(execMsg).toProperty("execMsg")
                        .map(alarmStatus).toProperty("alarmStatus")
        );
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int insertSelective(JobLog record) {
        return MyBatis3Utils.insert(this::insert, record, jobLog, c ->
                c.map(jobId).toPropertyWhenPresent("jobId", record::getJobId)
                        .map(groupName).toPropertyWhenPresent("groupName", record::getGroupName)
                        .map(executorAddress).toPropertyWhenPresent("executorAddress", record::getExecutorAddress)
                        .map(executorHandler).toPropertyWhenPresent("executorHandler", record::getExecutorHandler)
                        .map(executorParam).toPropertyWhenPresent("executorParam", record::getExecutorParam)
                        .map(failRetryCount).toPropertyWhenPresent("failRetryCount", record::getFailRetryCount)
                        .map(triggerTime).toPropertyWhenPresent("triggerTime", record::getTriggerTime)
                        .map(triggerCode).toPropertyWhenPresent("triggerCode", record::getTriggerCode)
                        .map(triggerMsg).toPropertyWhenPresent("triggerMsg", record::getTriggerMsg)
                        .map(execTime).toPropertyWhenPresent("execTime", record::getExecTime)
                        .map(execCode).toPropertyWhenPresent("execCode", record::getExecCode)
                        .map(execMsg).toPropertyWhenPresent("execMsg", record::getExecMsg)
                        .map(alarmStatus).toPropertyWhenPresent("alarmStatus", record::getAlarmStatus)
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
        return dsl.set(jobId).equalTo(record::getJobId)
                .set(groupName).equalTo(record::getGroupName)
                .set(executorAddress).equalTo(record::getExecutorAddress)
                .set(executorHandler).equalTo(record::getExecutorHandler)
                .set(executorParam).equalTo(record::getExecutorParam)
                .set(failRetryCount).equalTo(record::getFailRetryCount)
                .set(triggerTime).equalTo(record::getTriggerTime)
                .set(triggerCode).equalTo(record::getTriggerCode)
                .set(triggerMsg).equalTo(record::getTriggerMsg)
                .set(execTime).equalTo(record::getExecTime)
                .set(execCode).equalTo(record::getExecCode)
                .set(execMsg).equalTo(record::getExecMsg)
                .set(alarmStatus).equalTo(record::getAlarmStatus);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    static UpdateDSL<UpdateModel> updateSelectiveColumns(JobLog record, UpdateDSL<UpdateModel> dsl) {
        return dsl.set(jobId).equalToWhenPresent(record::getJobId)
                .set(groupName).equalToWhenPresent(record::getGroupName)
                .set(executorAddress).equalToWhenPresent(record::getExecutorAddress)
                .set(executorHandler).equalToWhenPresent(record::getExecutorHandler)
                .set(executorParam).equalToWhenPresent(record::getExecutorParam)
                .set(failRetryCount).equalToWhenPresent(record::getFailRetryCount)
                .set(triggerTime).equalToWhenPresent(record::getTriggerTime)
                .set(triggerCode).equalToWhenPresent(record::getTriggerCode)
                .set(triggerMsg).equalToWhenPresent(record::getTriggerMsg)
                .set(execTime).equalToWhenPresent(record::getExecTime)
                .set(execCode).equalToWhenPresent(record::getExecCode)
                .set(execMsg).equalToWhenPresent(record::getExecMsg)
                .set(alarmStatus).equalToWhenPresent(record::getAlarmStatus);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int updateByPrimaryKey(JobLog record) {
        return update(c ->
                c.set(jobId).equalTo(record::getJobId)
                        .set(groupName).equalTo(record::getGroupName)
                        .set(executorAddress).equalTo(record::getExecutorAddress)
                        .set(executorHandler).equalTo(record::getExecutorHandler)
                        .set(executorParam).equalTo(record::getExecutorParam)
                        .set(failRetryCount).equalTo(record::getFailRetryCount)
                        .set(triggerTime).equalTo(record::getTriggerTime)
                        .set(triggerCode).equalTo(record::getTriggerCode)
                        .set(triggerMsg).equalTo(record::getTriggerMsg)
                        .set(execTime).equalTo(record::getExecTime)
                        .set(execCode).equalTo(record::getExecCode)
                        .set(execMsg).equalTo(record::getExecMsg)
                        .set(alarmStatus).equalTo(record::getAlarmStatus)
                        .where(id, isEqualTo(record::getId))
        );
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int updateByPrimaryKeySelective(JobLog record) {
        return update(c ->
                c.set(jobId).equalToWhenPresent(record::getJobId)
                        .set(groupName).equalToWhenPresent(record::getGroupName)
                        .set(executorAddress).equalToWhenPresent(record::getExecutorAddress)
                        .set(executorHandler).equalToWhenPresent(record::getExecutorHandler)
                        .set(executorParam).equalToWhenPresent(record::getExecutorParam)
                        .set(failRetryCount).equalToWhenPresent(record::getFailRetryCount)
                        .set(triggerTime).equalToWhenPresent(record::getTriggerTime)
                        .set(triggerCode).equalToWhenPresent(record::getTriggerCode)
                        .set(triggerMsg).equalToWhenPresent(record::getTriggerMsg)
                        .set(execTime).equalToWhenPresent(record::getExecTime)
                        .set(execCode).equalToWhenPresent(record::getExecCode)
                        .set(execMsg).equalToWhenPresent(record::getExecMsg)
                        .set(alarmStatus).equalToWhenPresent(record::getAlarmStatus)
                        .where(id, isEqualTo(record::getId))
        );
    }
}