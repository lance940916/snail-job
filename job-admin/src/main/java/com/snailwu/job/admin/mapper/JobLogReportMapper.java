package com.snailwu.job.admin.mapper;

import com.snailwu.job.admin.model.JobLogReport;

import java.util.Date;
import java.util.List;

public interface JobLogReportMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(JobLogReport record);

    JobLogReport selectByPrimaryKey(Integer id);

    List<JobLogReport> selectAll();

    int updateByPrimaryKey(JobLogReport record);

    JobLogReport selectTodayReport(Date trigger_day);
}