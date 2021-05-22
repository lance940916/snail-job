package com.snailwu.job.admin.mapper;

import com.snailwu.job.admin.controller.request.JobGroupSearchRequest;
import com.snailwu.job.admin.model.JobApp;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface JobAppMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(JobApp record);

    JobApp selectByPrimaryKey(Integer id);

    List<JobApp> selectAll();

    int updateByPrimaryKey(JobApp record);

    List<JobApp> selectAutoRegistry(Byte type);

    void updateAddressesById(JobApp app);

    JobApp selectByAppName(String appName);

    List<JobApp> selectByCondition(JobGroupSearchRequest searchRequest);
}