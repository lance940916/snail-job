package com.snailwu.job.admin.dao;

import static com.snailwu.job.admin.dao.SnailJobInfoDynamicSqlSupport.*;
import static org.mybatis.dynamic.sql.SqlBuilder.*;

import com.snailwu.job.admin.core.model.SnailJobInfo;
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
public interface SnailJobInfoMapper {
    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    BasicColumn[] selectList = BasicColumn.columnList(id, jobGroup, jobCron, jobDesc, addTime, updateTime, author, alarmEmail, executorRouteStrategy, executorHandler, executorParam, executorBlockStrategy, executorTimeout, executorFailRetryCount, glueType, glueRemark, glueUpdatetime, childJobid, triggerStatus, triggerLastTime, triggerNextTime, glueSource);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    long count(SelectStatementProvider selectStatement);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    @DeleteProvider(type=SqlProviderAdapter.class, method="delete")
    int delete(DeleteStatementProvider deleteStatement);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    @InsertProvider(type=SqlProviderAdapter.class, method="insert")
    int insert(InsertStatementProvider<SnailJobInfo> insertStatement);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    @InsertProvider(type=SqlProviderAdapter.class, method="insertMultiple")
    int insertMultiple(MultiRowInsertStatementProvider<SnailJobInfo> multipleInsertStatement);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    @ResultMap("SnailJobInfoResult")
    Optional<SnailJobInfo> selectOne(SelectStatementProvider selectStatement);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    @Results(id="SnailJobInfoResult", value = {
        @Result(column="id", property="id", jdbcType=JdbcType.INTEGER, id=true),
        @Result(column="job_group", property="jobGroup", jdbcType=JdbcType.INTEGER),
        @Result(column="job_cron", property="jobCron", jdbcType=JdbcType.VARCHAR),
        @Result(column="job_desc", property="jobDesc", jdbcType=JdbcType.VARCHAR),
        @Result(column="add_time", property="addTime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="update_time", property="updateTime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="author", property="author", jdbcType=JdbcType.VARCHAR),
        @Result(column="alarm_email", property="alarmEmail", jdbcType=JdbcType.VARCHAR),
        @Result(column="executor_route_strategy", property="executorRouteStrategy", jdbcType=JdbcType.VARCHAR),
        @Result(column="executor_handler", property="executorHandler", jdbcType=JdbcType.VARCHAR),
        @Result(column="executor_param", property="executorParam", jdbcType=JdbcType.VARCHAR),
        @Result(column="executor_block_strategy", property="executorBlockStrategy", jdbcType=JdbcType.VARCHAR),
        @Result(column="executor_timeout", property="executorTimeout", jdbcType=JdbcType.INTEGER),
        @Result(column="executor_fail_retry_count", property="executorFailRetryCount", jdbcType=JdbcType.INTEGER),
        @Result(column="glue_type", property="glueType", jdbcType=JdbcType.VARCHAR),
        @Result(column="glue_remark", property="glueRemark", jdbcType=JdbcType.VARCHAR),
        @Result(column="glue_updatetime", property="glueUpdatetime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="child_jobid", property="childJobid", jdbcType=JdbcType.VARCHAR),
        @Result(column="trigger_status", property="triggerStatus", jdbcType=JdbcType.TINYINT),
        @Result(column="trigger_last_time", property="triggerLastTime", jdbcType=JdbcType.BIGINT),
        @Result(column="trigger_next_time", property="triggerNextTime", jdbcType=JdbcType.BIGINT),
        @Result(column="glue_source", property="glueSource", jdbcType=JdbcType.LONGVARCHAR)
    })
    List<SnailJobInfo> selectMany(SelectStatementProvider selectStatement);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    @UpdateProvider(type=SqlProviderAdapter.class, method="update")
    int update(UpdateStatementProvider updateStatement);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default long count(CountDSLCompleter completer) {
        return MyBatis3Utils.countFrom(this::count, snailJobInfo, completer);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int delete(DeleteDSLCompleter completer) {
        return MyBatis3Utils.deleteFrom(this::delete, snailJobInfo, completer);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int deleteByPrimaryKey(Integer id_) {
        return delete(c -> 
            c.where(id, isEqualTo(id_))
        );
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int insert(SnailJobInfo record) {
        return MyBatis3Utils.insert(this::insert, record, snailJobInfo, c ->
            c.map(id).toProperty("id")
            .map(jobGroup).toProperty("jobGroup")
            .map(jobCron).toProperty("jobCron")
            .map(jobDesc).toProperty("jobDesc")
            .map(addTime).toProperty("addTime")
            .map(updateTime).toProperty("updateTime")
            .map(author).toProperty("author")
            .map(alarmEmail).toProperty("alarmEmail")
            .map(executorRouteStrategy).toProperty("executorRouteStrategy")
            .map(executorHandler).toProperty("executorHandler")
            .map(executorParam).toProperty("executorParam")
            .map(executorBlockStrategy).toProperty("executorBlockStrategy")
            .map(executorTimeout).toProperty("executorTimeout")
            .map(executorFailRetryCount).toProperty("executorFailRetryCount")
            .map(glueType).toProperty("glueType")
            .map(glueRemark).toProperty("glueRemark")
            .map(glueUpdatetime).toProperty("glueUpdatetime")
            .map(childJobid).toProperty("childJobid")
            .map(triggerStatus).toProperty("triggerStatus")
            .map(triggerLastTime).toProperty("triggerLastTime")
            .map(triggerNextTime).toProperty("triggerNextTime")
            .map(glueSource).toProperty("glueSource")
        );
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int insertMultiple(Collection<SnailJobInfo> records) {
        return MyBatis3Utils.insertMultiple(this::insertMultiple, records, snailJobInfo, c ->
            c.map(id).toProperty("id")
            .map(jobGroup).toProperty("jobGroup")
            .map(jobCron).toProperty("jobCron")
            .map(jobDesc).toProperty("jobDesc")
            .map(addTime).toProperty("addTime")
            .map(updateTime).toProperty("updateTime")
            .map(author).toProperty("author")
            .map(alarmEmail).toProperty("alarmEmail")
            .map(executorRouteStrategy).toProperty("executorRouteStrategy")
            .map(executorHandler).toProperty("executorHandler")
            .map(executorParam).toProperty("executorParam")
            .map(executorBlockStrategy).toProperty("executorBlockStrategy")
            .map(executorTimeout).toProperty("executorTimeout")
            .map(executorFailRetryCount).toProperty("executorFailRetryCount")
            .map(glueType).toProperty("glueType")
            .map(glueRemark).toProperty("glueRemark")
            .map(glueUpdatetime).toProperty("glueUpdatetime")
            .map(childJobid).toProperty("childJobid")
            .map(triggerStatus).toProperty("triggerStatus")
            .map(triggerLastTime).toProperty("triggerLastTime")
            .map(triggerNextTime).toProperty("triggerNextTime")
            .map(glueSource).toProperty("glueSource")
        );
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int insertSelective(SnailJobInfo record) {
        return MyBatis3Utils.insert(this::insert, record, snailJobInfo, c ->
            c.map(id).toPropertyWhenPresent("id", record::getId)
            .map(jobGroup).toPropertyWhenPresent("jobGroup", record::getJobGroup)
            .map(jobCron).toPropertyWhenPresent("jobCron", record::getJobCron)
            .map(jobDesc).toPropertyWhenPresent("jobDesc", record::getJobDesc)
            .map(addTime).toPropertyWhenPresent("addTime", record::getAddTime)
            .map(updateTime).toPropertyWhenPresent("updateTime", record::getUpdateTime)
            .map(author).toPropertyWhenPresent("author", record::getAuthor)
            .map(alarmEmail).toPropertyWhenPresent("alarmEmail", record::getAlarmEmail)
            .map(executorRouteStrategy).toPropertyWhenPresent("executorRouteStrategy", record::getExecutorRouteStrategy)
            .map(executorHandler).toPropertyWhenPresent("executorHandler", record::getExecutorHandler)
            .map(executorParam).toPropertyWhenPresent("executorParam", record::getExecutorParam)
            .map(executorBlockStrategy).toPropertyWhenPresent("executorBlockStrategy", record::getExecutorBlockStrategy)
            .map(executorTimeout).toPropertyWhenPresent("executorTimeout", record::getExecutorTimeout)
            .map(executorFailRetryCount).toPropertyWhenPresent("executorFailRetryCount", record::getExecutorFailRetryCount)
            .map(glueType).toPropertyWhenPresent("glueType", record::getGlueType)
            .map(glueRemark).toPropertyWhenPresent("glueRemark", record::getGlueRemark)
            .map(glueUpdatetime).toPropertyWhenPresent("glueUpdatetime", record::getGlueUpdatetime)
            .map(childJobid).toPropertyWhenPresent("childJobid", record::getChildJobid)
            .map(triggerStatus).toPropertyWhenPresent("triggerStatus", record::getTriggerStatus)
            .map(triggerLastTime).toPropertyWhenPresent("triggerLastTime", record::getTriggerLastTime)
            .map(triggerNextTime).toPropertyWhenPresent("triggerNextTime", record::getTriggerNextTime)
            .map(glueSource).toPropertyWhenPresent("glueSource", record::getGlueSource)
        );
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default Optional<SnailJobInfo> selectOne(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectOne(this::selectOne, selectList, snailJobInfo, completer);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default List<SnailJobInfo> select(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectList(this::selectMany, selectList, snailJobInfo, completer);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default List<SnailJobInfo> selectDistinct(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectDistinct(this::selectMany, selectList, snailJobInfo, completer);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default Optional<SnailJobInfo> selectByPrimaryKey(Integer id_) {
        return selectOne(c ->
            c.where(id, isEqualTo(id_))
        );
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int update(UpdateDSLCompleter completer) {
        return MyBatis3Utils.update(this::update, snailJobInfo, completer);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    static UpdateDSL<UpdateModel> updateAllColumns(SnailJobInfo record, UpdateDSL<UpdateModel> dsl) {
        return dsl.set(id).equalTo(record::getId)
                .set(jobGroup).equalTo(record::getJobGroup)
                .set(jobCron).equalTo(record::getJobCron)
                .set(jobDesc).equalTo(record::getJobDesc)
                .set(addTime).equalTo(record::getAddTime)
                .set(updateTime).equalTo(record::getUpdateTime)
                .set(author).equalTo(record::getAuthor)
                .set(alarmEmail).equalTo(record::getAlarmEmail)
                .set(executorRouteStrategy).equalTo(record::getExecutorRouteStrategy)
                .set(executorHandler).equalTo(record::getExecutorHandler)
                .set(executorParam).equalTo(record::getExecutorParam)
                .set(executorBlockStrategy).equalTo(record::getExecutorBlockStrategy)
                .set(executorTimeout).equalTo(record::getExecutorTimeout)
                .set(executorFailRetryCount).equalTo(record::getExecutorFailRetryCount)
                .set(glueType).equalTo(record::getGlueType)
                .set(glueRemark).equalTo(record::getGlueRemark)
                .set(glueUpdatetime).equalTo(record::getGlueUpdatetime)
                .set(childJobid).equalTo(record::getChildJobid)
                .set(triggerStatus).equalTo(record::getTriggerStatus)
                .set(triggerLastTime).equalTo(record::getTriggerLastTime)
                .set(triggerNextTime).equalTo(record::getTriggerNextTime)
                .set(glueSource).equalTo(record::getGlueSource);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    static UpdateDSL<UpdateModel> updateSelectiveColumns(SnailJobInfo record, UpdateDSL<UpdateModel> dsl) {
        return dsl.set(id).equalToWhenPresent(record::getId)
                .set(jobGroup).equalToWhenPresent(record::getJobGroup)
                .set(jobCron).equalToWhenPresent(record::getJobCron)
                .set(jobDesc).equalToWhenPresent(record::getJobDesc)
                .set(addTime).equalToWhenPresent(record::getAddTime)
                .set(updateTime).equalToWhenPresent(record::getUpdateTime)
                .set(author).equalToWhenPresent(record::getAuthor)
                .set(alarmEmail).equalToWhenPresent(record::getAlarmEmail)
                .set(executorRouteStrategy).equalToWhenPresent(record::getExecutorRouteStrategy)
                .set(executorHandler).equalToWhenPresent(record::getExecutorHandler)
                .set(executorParam).equalToWhenPresent(record::getExecutorParam)
                .set(executorBlockStrategy).equalToWhenPresent(record::getExecutorBlockStrategy)
                .set(executorTimeout).equalToWhenPresent(record::getExecutorTimeout)
                .set(executorFailRetryCount).equalToWhenPresent(record::getExecutorFailRetryCount)
                .set(glueType).equalToWhenPresent(record::getGlueType)
                .set(glueRemark).equalToWhenPresent(record::getGlueRemark)
                .set(glueUpdatetime).equalToWhenPresent(record::getGlueUpdatetime)
                .set(childJobid).equalToWhenPresent(record::getChildJobid)
                .set(triggerStatus).equalToWhenPresent(record::getTriggerStatus)
                .set(triggerLastTime).equalToWhenPresent(record::getTriggerLastTime)
                .set(triggerNextTime).equalToWhenPresent(record::getTriggerNextTime)
                .set(glueSource).equalToWhenPresent(record::getGlueSource);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int updateByPrimaryKey(SnailJobInfo record) {
        return update(c ->
            c.set(jobGroup).equalTo(record::getJobGroup)
            .set(jobCron).equalTo(record::getJobCron)
            .set(jobDesc).equalTo(record::getJobDesc)
            .set(addTime).equalTo(record::getAddTime)
            .set(updateTime).equalTo(record::getUpdateTime)
            .set(author).equalTo(record::getAuthor)
            .set(alarmEmail).equalTo(record::getAlarmEmail)
            .set(executorRouteStrategy).equalTo(record::getExecutorRouteStrategy)
            .set(executorHandler).equalTo(record::getExecutorHandler)
            .set(executorParam).equalTo(record::getExecutorParam)
            .set(executorBlockStrategy).equalTo(record::getExecutorBlockStrategy)
            .set(executorTimeout).equalTo(record::getExecutorTimeout)
            .set(executorFailRetryCount).equalTo(record::getExecutorFailRetryCount)
            .set(glueType).equalTo(record::getGlueType)
            .set(glueRemark).equalTo(record::getGlueRemark)
            .set(glueUpdatetime).equalTo(record::getGlueUpdatetime)
            .set(childJobid).equalTo(record::getChildJobid)
            .set(triggerStatus).equalTo(record::getTriggerStatus)
            .set(triggerLastTime).equalTo(record::getTriggerLastTime)
            .set(triggerNextTime).equalTo(record::getTriggerNextTime)
            .set(glueSource).equalTo(record::getGlueSource)
            .where(id, isEqualTo(record::getId))
        );
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int updateByPrimaryKeySelective(SnailJobInfo record) {
        return update(c ->
            c.set(jobGroup).equalToWhenPresent(record::getJobGroup)
            .set(jobCron).equalToWhenPresent(record::getJobCron)
            .set(jobDesc).equalToWhenPresent(record::getJobDesc)
            .set(addTime).equalToWhenPresent(record::getAddTime)
            .set(updateTime).equalToWhenPresent(record::getUpdateTime)
            .set(author).equalToWhenPresent(record::getAuthor)
            .set(alarmEmail).equalToWhenPresent(record::getAlarmEmail)
            .set(executorRouteStrategy).equalToWhenPresent(record::getExecutorRouteStrategy)
            .set(executorHandler).equalToWhenPresent(record::getExecutorHandler)
            .set(executorParam).equalToWhenPresent(record::getExecutorParam)
            .set(executorBlockStrategy).equalToWhenPresent(record::getExecutorBlockStrategy)
            .set(executorTimeout).equalToWhenPresent(record::getExecutorTimeout)
            .set(executorFailRetryCount).equalToWhenPresent(record::getExecutorFailRetryCount)
            .set(glueType).equalToWhenPresent(record::getGlueType)
            .set(glueRemark).equalToWhenPresent(record::getGlueRemark)
            .set(glueUpdatetime).equalToWhenPresent(record::getGlueUpdatetime)
            .set(childJobid).equalToWhenPresent(record::getChildJobid)
            .set(triggerStatus).equalToWhenPresent(record::getTriggerStatus)
            .set(triggerLastTime).equalToWhenPresent(record::getTriggerLastTime)
            .set(triggerNextTime).equalToWhenPresent(record::getTriggerNextTime)
            .set(glueSource).equalToWhenPresent(record::getGlueSource)
            .where(id, isEqualTo(record::getId))
        );
    }
}