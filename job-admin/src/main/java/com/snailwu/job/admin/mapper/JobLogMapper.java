package com.snailwu.job.admin.mapper;

import com.snailwu.job.admin.controller.request.JobLogDeleteRequest;
import com.snailwu.job.admin.controller.request.JobLogSearchRequest;
import com.snailwu.job.admin.controller.vo.JobLogVO;
import com.snailwu.job.admin.model.JobLog;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface JobLogMapper {
    int deleteByPrimaryKey(Long id);

    int insert(JobLog record);

    JobLog selectByPrimaryKey(Long id);

    List<JobLog> selectAll();

    int updateByPrimaryKey(JobLog record);

    List<JobLog> selectNeedAlarm(@Param("alarmStatus") Byte alarmStatus,
                                 @Param("successCode") int successCode);

    void updateAlarmStatusById(@Param("id") Long id, @Param("alarmStatus") Byte alarmStatus);

    List<JobLog> selectTodayLogs(@Param("beginTime") Date beginTime, @Param("endTime") Date endTime);

    void updateTriggerResultById(JobLog log);

    JobLog selectExecCodeById(long id);

    void updateExecResultById(JobLog log);

    List<JobLogVO> selectByCondition(JobLogSearchRequest searchRequest);

    void deleteByCondition(JobLogDeleteRequest dr);
}