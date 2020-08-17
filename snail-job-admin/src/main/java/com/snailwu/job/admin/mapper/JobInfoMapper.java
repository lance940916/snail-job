package com.snailwu.job.admin.mapper;

import com.snailwu.job.admin.core.model.JobInfo;
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

import static com.snailwu.job.admin.mapper.JobInfoDynamicSqlSupport.*;
import static org.mybatis.dynamic.sql.SqlBuilder.isEqualTo;

@Mapper
public interface JobInfoMapper {
    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    BasicColumn[] selectList = BasicColumn.columnList(id, name, groupName, cron, createTime, updateTime, author, alarmEmail, executorRouteStrategy, executorHandler, executorParam, executorTimeout, executorFailRetryCount, triggerStatus, triggerLastTime, triggerNextTime);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    @SelectProvider(type = SqlProviderAdapter.class, method = "select")
    long count(SelectStatementProvider selectStatement);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    @DeleteProvider(type = SqlProviderAdapter.class, method = "delete")
    int delete(DeleteStatementProvider deleteStatement);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    @InsertProvider(type = SqlProviderAdapter.class, method = "insert")
    @SelectKey(statement = "SELECT LAST_INSERT_ID()", keyProperty = "record.id", before = false, resultType = Integer.class)
    int insert(InsertStatementProvider<JobInfo> insertStatement);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    @SelectProvider(type = SqlProviderAdapter.class, method = "select")
    @ResultMap("JobInfoResult")
    Optional<JobInfo> selectOne(SelectStatementProvider selectStatement);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    @SelectProvider(type = SqlProviderAdapter.class, method = "select")
    @Results(id = "JobInfoResult", value = {
            @Result(column = "id", property = "id", jdbcType = JdbcType.INTEGER, id = true),
            @Result(column = "name", property = "name", jdbcType = JdbcType.VARCHAR),
            @Result(column = "group_name", property = "groupName", jdbcType = JdbcType.VARCHAR),
            @Result(column = "cron", property = "cron", jdbcType = JdbcType.VARCHAR),
            @Result(column = "create_time", property = "createTime", jdbcType = JdbcType.TIMESTAMP),
            @Result(column = "update_time", property = "updateTime", jdbcType = JdbcType.TIMESTAMP),
            @Result(column = "author", property = "author", jdbcType = JdbcType.VARCHAR),
            @Result(column = "alarm_email", property = "alarmEmail", jdbcType = JdbcType.VARCHAR),
            @Result(column = "executor_route_strategy", property = "executorRouteStrategy", jdbcType = JdbcType.VARCHAR),
            @Result(column = "executor_handler", property = "executorHandler", jdbcType = JdbcType.VARCHAR),
            @Result(column = "executor_param", property = "executorParam", jdbcType = JdbcType.VARCHAR),
            @Result(column = "executor_timeout", property = "executorTimeout", jdbcType = JdbcType.INTEGER),
            @Result(column = "executor_fail_retry_count", property = "executorFailRetryCount", jdbcType = JdbcType.TINYINT),
            @Result(column = "trigger_status", property = "triggerStatus", jdbcType = JdbcType.TINYINT),
            @Result(column = "trigger_last_time", property = "triggerLastTime", jdbcType = JdbcType.BIGINT),
            @Result(column = "trigger_next_time", property = "triggerNextTime", jdbcType = JdbcType.BIGINT)
    })
    List<JobInfo> selectMany(SelectStatementProvider selectStatement);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    @UpdateProvider(type = SqlProviderAdapter.class, method = "update")
    int update(UpdateStatementProvider updateStatement);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default long count(CountDSLCompleter completer) {
        return MyBatis3Utils.countFrom(this::count, jobInfo, completer);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int delete(DeleteDSLCompleter completer) {
        return MyBatis3Utils.deleteFrom(this::delete, jobInfo, completer);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int deleteByPrimaryKey(Integer id_) {
        return delete(c ->
                c.where(id, isEqualTo(id_))
        );
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int insert(JobInfo record) {
        return MyBatis3Utils.insert(this::insert, record, jobInfo, c ->
                c.map(name).toProperty("name")
                        .map(groupName).toProperty("groupName")
                        .map(cron).toProperty("cron")
                        .map(createTime).toProperty("createTime")
                        .map(updateTime).toProperty("updateTime")
                        .map(author).toProperty("author")
                        .map(alarmEmail).toProperty("alarmEmail")
                        .map(executorRouteStrategy).toProperty("executorRouteStrategy")
                        .map(executorHandler).toProperty("executorHandler")
                        .map(executorParam).toProperty("executorParam")
                        .map(executorTimeout).toProperty("executorTimeout")
                        .map(executorFailRetryCount).toProperty("executorFailRetryCount")
                        .map(triggerStatus).toProperty("triggerStatus")
                        .map(triggerLastTime).toProperty("triggerLastTime")
                        .map(triggerNextTime).toProperty("triggerNextTime")
        );
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int insertSelective(JobInfo record) {
        return MyBatis3Utils.insert(this::insert, record, jobInfo, c ->
                c.map(name).toPropertyWhenPresent("name", record::getName)
                        .map(groupName).toPropertyWhenPresent("groupName", record::getGroupName)
                        .map(cron).toPropertyWhenPresent("cron", record::getCron)
                        .map(createTime).toPropertyWhenPresent("createTime", record::getCreateTime)
                        .map(updateTime).toPropertyWhenPresent("updateTime", record::getUpdateTime)
                        .map(author).toPropertyWhenPresent("author", record::getAuthor)
                        .map(alarmEmail).toPropertyWhenPresent("alarmEmail", record::getAlarmEmail)
                        .map(executorRouteStrategy).toPropertyWhenPresent("executorRouteStrategy", record::getExecutorRouteStrategy)
                        .map(executorHandler).toPropertyWhenPresent("executorHandler", record::getExecutorHandler)
                        .map(executorParam).toPropertyWhenPresent("executorParam", record::getExecutorParam)
                        .map(executorTimeout).toPropertyWhenPresent("executorTimeout", record::getExecutorTimeout)
                        .map(executorFailRetryCount).toPropertyWhenPresent("executorFailRetryCount", record::getExecutorFailRetryCount)
                        .map(triggerStatus).toPropertyWhenPresent("triggerStatus", record::getTriggerStatus)
                        .map(triggerLastTime).toPropertyWhenPresent("triggerLastTime", record::getTriggerLastTime)
                        .map(triggerNextTime).toPropertyWhenPresent("triggerNextTime", record::getTriggerNextTime)
        );
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default Optional<JobInfo> selectOne(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectOne(this::selectOne, selectList, jobInfo, completer);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default List<JobInfo> select(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectList(this::selectMany, selectList, jobInfo, completer);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default List<JobInfo> selectDistinct(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectDistinct(this::selectMany, selectList, jobInfo, completer);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default Optional<JobInfo> selectByPrimaryKey(Integer id_) {
        return selectOne(c ->
                c.where(id, isEqualTo(id_))
        );
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int update(UpdateDSLCompleter completer) {
        return MyBatis3Utils.update(this::update, jobInfo, completer);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    static UpdateDSL<UpdateModel> updateAllColumns(JobInfo record, UpdateDSL<UpdateModel> dsl) {
        return dsl.set(name).equalTo(record::getName)
                .set(groupName).equalTo(record::getGroupName)
                .set(cron).equalTo(record::getCron)
                .set(createTime).equalTo(record::getCreateTime)
                .set(updateTime).equalTo(record::getUpdateTime)
                .set(author).equalTo(record::getAuthor)
                .set(alarmEmail).equalTo(record::getAlarmEmail)
                .set(executorRouteStrategy).equalTo(record::getExecutorRouteStrategy)
                .set(executorHandler).equalTo(record::getExecutorHandler)
                .set(executorParam).equalTo(record::getExecutorParam)
                .set(executorTimeout).equalTo(record::getExecutorTimeout)
                .set(executorFailRetryCount).equalTo(record::getExecutorFailRetryCount)
                .set(triggerStatus).equalTo(record::getTriggerStatus)
                .set(triggerLastTime).equalTo(record::getTriggerLastTime)
                .set(triggerNextTime).equalTo(record::getTriggerNextTime);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    static UpdateDSL<UpdateModel> updateSelectiveColumns(JobInfo record, UpdateDSL<UpdateModel> dsl) {
        return dsl.set(name).equalToWhenPresent(record::getName)
                .set(groupName).equalToWhenPresent(record::getGroupName)
                .set(cron).equalToWhenPresent(record::getCron)
                .set(createTime).equalToWhenPresent(record::getCreateTime)
                .set(updateTime).equalToWhenPresent(record::getUpdateTime)
                .set(author).equalToWhenPresent(record::getAuthor)
                .set(alarmEmail).equalToWhenPresent(record::getAlarmEmail)
                .set(executorRouteStrategy).equalToWhenPresent(record::getExecutorRouteStrategy)
                .set(executorHandler).equalToWhenPresent(record::getExecutorHandler)
                .set(executorParam).equalToWhenPresent(record::getExecutorParam)
                .set(executorTimeout).equalToWhenPresent(record::getExecutorTimeout)
                .set(executorFailRetryCount).equalToWhenPresent(record::getExecutorFailRetryCount)
                .set(triggerStatus).equalToWhenPresent(record::getTriggerStatus)
                .set(triggerLastTime).equalToWhenPresent(record::getTriggerLastTime)
                .set(triggerNextTime).equalToWhenPresent(record::getTriggerNextTime);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int updateByPrimaryKey(JobInfo record) {
        return update(c ->
                c.set(name).equalTo(record::getName)
                        .set(groupName).equalTo(record::getGroupName)
                        .set(cron).equalTo(record::getCron)
                        .set(createTime).equalTo(record::getCreateTime)
                        .set(updateTime).equalTo(record::getUpdateTime)
                        .set(author).equalTo(record::getAuthor)
                        .set(alarmEmail).equalTo(record::getAlarmEmail)
                        .set(executorRouteStrategy).equalTo(record::getExecutorRouteStrategy)
                        .set(executorHandler).equalTo(record::getExecutorHandler)
                        .set(executorParam).equalTo(record::getExecutorParam)
                        .set(executorTimeout).equalTo(record::getExecutorTimeout)
                        .set(executorFailRetryCount).equalTo(record::getExecutorFailRetryCount)
                        .set(triggerStatus).equalTo(record::getTriggerStatus)
                        .set(triggerLastTime).equalTo(record::getTriggerLastTime)
                        .set(triggerNextTime).equalTo(record::getTriggerNextTime)
                        .where(id, isEqualTo(record::getId))
        );
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int updateByPrimaryKeySelective(JobInfo record) {
        return update(c ->
                c.set(name).equalToWhenPresent(record::getName)
                        .set(groupName).equalToWhenPresent(record::getGroupName)
                        .set(cron).equalToWhenPresent(record::getCron)
                        .set(createTime).equalToWhenPresent(record::getCreateTime)
                        .set(updateTime).equalToWhenPresent(record::getUpdateTime)
                        .set(author).equalToWhenPresent(record::getAuthor)
                        .set(alarmEmail).equalToWhenPresent(record::getAlarmEmail)
                        .set(executorRouteStrategy).equalToWhenPresent(record::getExecutorRouteStrategy)
                        .set(executorHandler).equalToWhenPresent(record::getExecutorHandler)
                        .set(executorParam).equalToWhenPresent(record::getExecutorParam)
                        .set(executorTimeout).equalToWhenPresent(record::getExecutorTimeout)
                        .set(executorFailRetryCount).equalToWhenPresent(record::getExecutorFailRetryCount)
                        .set(triggerStatus).equalToWhenPresent(record::getTriggerStatus)
                        .set(triggerLastTime).equalToWhenPresent(record::getTriggerLastTime)
                        .set(triggerNextTime).equalToWhenPresent(record::getTriggerNextTime)
                        .where(id, isEqualTo(record::getId))
        );
    }
}