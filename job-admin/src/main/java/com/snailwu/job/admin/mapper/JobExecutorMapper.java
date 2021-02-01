package com.snailwu.job.admin.mapper;

import com.snailwu.job.admin.model.JobExecutor;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface JobExecutorMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(JobExecutor record);

    JobExecutor selectByPrimaryKey(Integer id);

    List<JobExecutor> selectAll();

    int updateByPrimaryKey(JobExecutor record);

    void deleteDead(Date deadDate);

    JobExecutor selectByAppNameAndAddress(@Param("appName") String appName, @Param("address") String address);

    void deleteByAppNameAndAddress(@Param("appName") String appName, @Param("address") String address);

    List<JobExecutor> selectNeedSortOut();
}