package com.snailwu.job.admin.mapper;

import com.snailwu.job.admin.model.JobLock;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface JobLockMapper {
    int deleteByPrimaryKey(String lockName);

    int insert(JobLock record);

    List<JobLock> selectAll();
}