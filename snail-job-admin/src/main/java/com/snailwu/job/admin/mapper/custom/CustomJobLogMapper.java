package com.snailwu.job.admin.mapper.custom;

import com.snailwu.job.admin.model.JobLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.type.JdbcType;
import org.mybatis.dynamic.sql.select.render.SelectStatementProvider;
import org.mybatis.dynamic.sql.util.SqlProviderAdapter;

import java.util.List;

/**
 * @author 吴庆龙
 * @date 2020/11/27 上午10:19
 */
@Mapper
public interface CustomJobLogMapper {

    @SelectProvider(type= SqlProviderAdapter.class, method="select")
    @Results(id="JobLogResult", value = {
            @Result(column="id", property="id", jdbcType= JdbcType.BIGINT, id=true),
            @Result(column="job_id", property="jobId", jdbcType=JdbcType.INTEGER),
            @Result(column="app_name", property="appName", jdbcType=JdbcType.VARCHAR),
            @Result(column="exec_address", property="execAddress", jdbcType=JdbcType.VARCHAR),
            @Result(column="exec_handler", property="execHandler", jdbcType=JdbcType.VARCHAR),
            @Result(column="exec_param", property="execParam", jdbcType=JdbcType.VARCHAR),
            @Result(column="fail_retry_count", property="failRetryCount", jdbcType=JdbcType.TINYINT),
            @Result(column="trigger_time", property="triggerTime", jdbcType=JdbcType.TIMESTAMP),
            @Result(column="trigger_code", property="triggerCode", jdbcType=JdbcType.INTEGER),
            @Result(column="trigger_msg", property="triggerMsg", jdbcType=JdbcType.VARCHAR),
            @Result(column="exec_time", property="execTime", jdbcType=JdbcType.TIMESTAMP),
            @Result(column="exec_code", property="execCode", jdbcType=JdbcType.INTEGER),
            @Result(column="exec_msg", property="execMsg", jdbcType=JdbcType.VARCHAR),
            @Result(column="alarm_status", property="alarmStatus", jdbcType=JdbcType.TINYINT),
            @Result(column="job_name", property="jobName", jdbcType=JdbcType.TINYINT)
    })
    List<JobLog> selectManyWithJobName(SelectStatementProvider selectStatement);

}
