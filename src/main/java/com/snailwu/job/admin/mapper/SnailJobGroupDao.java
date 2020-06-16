package com.snailwu.job.admin.mapper;

import com.snailwu.job.admin.core.model.SnailJobGroup;

public interface SnailJobGroupDao {
    int deleteByPrimaryKey(Integer id);

    int insert(SnailJobGroup record);

    int insertSelective(SnailJobGroup record);

    SnailJobGroup selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SnailJobGroup record);

    int updateByPrimaryKey(SnailJobGroup record);
}