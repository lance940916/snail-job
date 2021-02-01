package com.snailwu.job.admin.mapper;

import com.snailwu.job.admin.controller.request.JobInfoSearchRequest;
import com.snailwu.job.admin.model.JobInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface JobInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(JobInfo record);

    JobInfo selectByPrimaryKey(Integer id);

    List<JobInfo> selectAll();

    int updateByPrimaryKey(JobInfo record);

    List<JobInfo> selectTriggerAtTime(@Param("triggerStatus") Byte triggerStatus,
                                      @Param("maxTriggerTime") Long maxTriggerTime);

    void updateNextTimeById(@Param("triggerLastTime") Long triggerLastTime,
                            @Param("triggerNextTime") Long triggerNextTime,
                            @Param("triggerStatus") Byte triggerStatus,
                            @Param("id") Integer id);

    List<JobInfo> selectByCondition(JobInfoSearchRequest searchRequest);

    List<JobInfo> selectByAppName(String appName);
}