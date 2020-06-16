package com.snailwu.job.admin.mapper;

import com.snailwu.job.admin.core.model.SnailJobGroup;
import com.snailwu.job.admin.core.model.SnailJobGroupExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface SnailJobGroupMapper {
    long countByExample(SnailJobGroupExample example);

    int deleteByExample(SnailJobGroupExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(SnailJobGroup record);

    int insertSelective(SnailJobGroup record);

    List<SnailJobGroup> selectByExample(SnailJobGroupExample example);

    SnailJobGroup selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") SnailJobGroup record, @Param("example") SnailJobGroupExample example);

    int updateByExample(@Param("record") SnailJobGroup record, @Param("example") SnailJobGroupExample example);

    int updateByPrimaryKeySelective(SnailJobGroup record);

    int updateByPrimaryKey(SnailJobGroup record);
}