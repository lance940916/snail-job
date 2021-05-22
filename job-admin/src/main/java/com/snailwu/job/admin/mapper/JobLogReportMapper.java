package com.snailwu.job.admin.mapper;

import com.snailwu.job.admin.model.JobLogReport;
import org.apache.ibatis.annotations.Mapper;

import java.util.Date;
import java.util.List;

@Mapper
public interface JobLogReportMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(JobLogReport record);

    JobLogReport selectByPrimaryKey(Integer id);

    List<JobLogReport> selectAll();

    int updateByPrimaryKey(JobLogReport record);

    JobLogReport selectTodayReport(Date trigger_day);
}