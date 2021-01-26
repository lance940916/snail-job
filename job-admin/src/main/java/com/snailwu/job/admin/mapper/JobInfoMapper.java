package com.snailwu.job.admin.mapper;

import com.snailwu.job.admin.model.JobInfo;
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
    BasicColumn[] selectList = BasicColumn.columnList(id, name, appName, cron, createTime, updateTime, author, alarmEmail, execRouteStrategy, execHandler, execParam, execTimeout, execFailRetryCount, triggerStatus, triggerLastTime, triggerNextTime);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    long count(SelectStatementProvider selectStatement);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    @DeleteProvider(type=SqlProviderAdapter.class, method="delete")
    int delete(DeleteStatementProvider deleteStatement);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    @InsertProvider(type=SqlProviderAdapter.class, method="insert")
    @SelectKey(statement="SELECT LAST_INSERT_ID()", keyProperty="record.id", before=false, resultType=Integer.class)
    int insert(InsertStatementProvider<JobInfo> insertStatement);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    @ResultMap("JobInfoResult")
    Optional<JobInfo> selectOne(SelectStatementProvider selectStatement);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    @Results(id="JobInfoResult", value = {
        @Result(column="id", property="id", jdbcType=JdbcType.INTEGER, id=true),
        @Result(column="name", property="name", jdbcType=JdbcType.VARCHAR),
        @Result(column="app_name", property="appName", jdbcType=JdbcType.VARCHAR),
        @Result(column="cron", property="cron", jdbcType=JdbcType.VARCHAR),
        @Result(column="create_time", property="createTime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="update_time", property="updateTime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="author", property="author", jdbcType=JdbcType.VARCHAR),
        @Result(column="alarm_email", property="alarmEmail", jdbcType=JdbcType.VARCHAR),
        @Result(column="exec_route_strategy", property="execRouteStrategy", jdbcType=JdbcType.VARCHAR),
        @Result(column="exec_handler", property="execHandler", jdbcType=JdbcType.VARCHAR),
        @Result(column="exec_param", property="execParam", jdbcType=JdbcType.VARCHAR),
        @Result(column="exec_timeout", property="execTimeout", jdbcType=JdbcType.INTEGER),
        @Result(column="exec_fail_retry_count", property="execFailRetryCount", jdbcType=JdbcType.TINYINT),
        @Result(column="trigger_status", property="triggerStatus", jdbcType=JdbcType.TINYINT),
        @Result(column="trigger_last_time", property="triggerLastTime", jdbcType=JdbcType.BIGINT),
        @Result(column="trigger_next_time", property="triggerNextTime", jdbcType=JdbcType.BIGINT)
    })
    List<JobInfo> selectMany(SelectStatementProvider selectStatement);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    @UpdateProvider(type=SqlProviderAdapter.class, method="update")
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
            .map(appName).toProperty("appName")
            .map(cron).toProperty("cron")
            .map(createTime).toProperty("createTime")
            .map(updateTime).toProperty("updateTime")
            .map(author).toProperty("author")
            .map(alarmEmail).toProperty("alarmEmail")
            .map(execRouteStrategy).toProperty("execRouteStrategy")
            .map(execHandler).toProperty("execHandler")
            .map(execParam).toProperty("execParam")
            .map(execTimeout).toProperty("execTimeout")
            .map(execFailRetryCount).toProperty("execFailRetryCount")
            .map(triggerStatus).toProperty("triggerStatus")
            .map(triggerLastTime).toProperty("triggerLastTime")
            .map(triggerNextTime).toProperty("triggerNextTime")
        );
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int insertSelective(JobInfo record) {
        return MyBatis3Utils.insert(this::insert, record, jobInfo, c ->
            c.map(name).toPropertyWhenPresent("name", record::getName)
            .map(appName).toPropertyWhenPresent("appName", record::getAppName)
            .map(cron).toPropertyWhenPresent("cron", record::getCron)
            .map(createTime).toPropertyWhenPresent("createTime", record::getCreateTime)
            .map(updateTime).toPropertyWhenPresent("updateTime", record::getUpdateTime)
            .map(author).toPropertyWhenPresent("author", record::getAuthor)
            .map(alarmEmail).toPropertyWhenPresent("alarmEmail", record::getAlarmEmail)
            .map(execRouteStrategy).toPropertyWhenPresent("execRouteStrategy", record::getExecRouteStrategy)
            .map(execHandler).toPropertyWhenPresent("execHandler", record::getExecHandler)
            .map(execParam).toPropertyWhenPresent("execParam", record::getExecParam)
            .map(execTimeout).toPropertyWhenPresent("execTimeout", record::getExecTimeout)
            .map(execFailRetryCount).toPropertyWhenPresent("execFailRetryCount", record::getExecFailRetryCount)
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
                .set(appName).equalTo(record::getAppName)
                .set(cron).equalTo(record::getCron)
                .set(createTime).equalTo(record::getCreateTime)
                .set(updateTime).equalTo(record::getUpdateTime)
                .set(author).equalTo(record::getAuthor)
                .set(alarmEmail).equalTo(record::getAlarmEmail)
                .set(execRouteStrategy).equalTo(record::getExecRouteStrategy)
                .set(execHandler).equalTo(record::getExecHandler)
                .set(execParam).equalTo(record::getExecParam)
                .set(execTimeout).equalTo(record::getExecTimeout)
                .set(execFailRetryCount).equalTo(record::getExecFailRetryCount)
                .set(triggerStatus).equalTo(record::getTriggerStatus)
                .set(triggerLastTime).equalTo(record::getTriggerLastTime)
                .set(triggerNextTime).equalTo(record::getTriggerNextTime);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    static UpdateDSL<UpdateModel> updateSelectiveColumns(JobInfo record, UpdateDSL<UpdateModel> dsl) {
        return dsl.set(name).equalToWhenPresent(record::getName)
                .set(appName).equalToWhenPresent(record::getAppName)
                .set(cron).equalToWhenPresent(record::getCron)
                .set(createTime).equalToWhenPresent(record::getCreateTime)
                .set(updateTime).equalToWhenPresent(record::getUpdateTime)
                .set(author).equalToWhenPresent(record::getAuthor)
                .set(alarmEmail).equalToWhenPresent(record::getAlarmEmail)
                .set(execRouteStrategy).equalToWhenPresent(record::getExecRouteStrategy)
                .set(execHandler).equalToWhenPresent(record::getExecHandler)
                .set(execParam).equalToWhenPresent(record::getExecParam)
                .set(execTimeout).equalToWhenPresent(record::getExecTimeout)
                .set(execFailRetryCount).equalToWhenPresent(record::getExecFailRetryCount)
                .set(triggerStatus).equalToWhenPresent(record::getTriggerStatus)
                .set(triggerLastTime).equalToWhenPresent(record::getTriggerLastTime)
                .set(triggerNextTime).equalToWhenPresent(record::getTriggerNextTime);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int updateByPrimaryKey(JobInfo record) {
        return update(c ->
            c.set(name).equalTo(record::getName)
            .set(appName).equalTo(record::getAppName)
            .set(cron).equalTo(record::getCron)
            .set(createTime).equalTo(record::getCreateTime)
            .set(updateTime).equalTo(record::getUpdateTime)
            .set(author).equalTo(record::getAuthor)
            .set(alarmEmail).equalTo(record::getAlarmEmail)
            .set(execRouteStrategy).equalTo(record::getExecRouteStrategy)
            .set(execHandler).equalTo(record::getExecHandler)
            .set(execParam).equalTo(record::getExecParam)
            .set(execTimeout).equalTo(record::getExecTimeout)
            .set(execFailRetryCount).equalTo(record::getExecFailRetryCount)
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
            .set(appName).equalToWhenPresent(record::getAppName)
            .set(cron).equalToWhenPresent(record::getCron)
            .set(createTime).equalToWhenPresent(record::getCreateTime)
            .set(updateTime).equalToWhenPresent(record::getUpdateTime)
            .set(author).equalToWhenPresent(record::getAuthor)
            .set(alarmEmail).equalToWhenPresent(record::getAlarmEmail)
            .set(execRouteStrategy).equalToWhenPresent(record::getExecRouteStrategy)
            .set(execHandler).equalToWhenPresent(record::getExecHandler)
            .set(execParam).equalToWhenPresent(record::getExecParam)
            .set(execTimeout).equalToWhenPresent(record::getExecTimeout)
            .set(execFailRetryCount).equalToWhenPresent(record::getExecFailRetryCount)
            .set(triggerStatus).equalToWhenPresent(record::getTriggerStatus)
            .set(triggerLastTime).equalToWhenPresent(record::getTriggerLastTime)
            .set(triggerNextTime).equalToWhenPresent(record::getTriggerNextTime)
            .where(id, isEqualTo(record::getId))
        );
    }
}