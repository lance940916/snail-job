package com.snailwu.job.admin.dao;

import com.snailwu.job.admin.core.model.SnailJobLock;
import com.snailwu.job.admin.core.model.SnailJobLockExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface SnailJobLockMapper {
    long countByExample(SnailJobLockExample example);

    int deleteByExample(SnailJobLockExample example);

    int deleteByPrimaryKey(String lockName);

    int insert(SnailJobLock record);

    int insertSelective(SnailJobLock record);

    List<SnailJobLock> selectByExample(SnailJobLockExample example);

    int updateByExampleSelective(@Param("record") SnailJobLock record, @Param("example") SnailJobLockExample example);

    int updateByExample(@Param("record") SnailJobLock record, @Param("example") SnailJobLockExample example);
}