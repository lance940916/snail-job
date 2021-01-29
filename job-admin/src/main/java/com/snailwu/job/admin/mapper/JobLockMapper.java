package com.snailwu.job.admin.mapper;

import com.snailwu.job.admin.model.JobLock;
import java.util.List;

public interface JobLockMapper {
    int deleteByPrimaryKey(String lockName);

    int insert(JobLock record);

    List<JobLock> selectAll();
}