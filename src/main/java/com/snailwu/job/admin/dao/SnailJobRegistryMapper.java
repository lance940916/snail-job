package com.snailwu.job.admin.dao;

import com.snailwu.job.admin.core.model.SnailJobRegistry;
import com.snailwu.job.admin.core.model.SnailJobRegistryExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface SnailJobRegistryMapper {
    long countByExample(SnailJobRegistryExample example);

    int deleteByExample(SnailJobRegistryExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(SnailJobRegistry record);

    int insertSelective(SnailJobRegistry record);

    List<SnailJobRegistry> selectByExample(SnailJobRegistryExample example);

    SnailJobRegistry selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") SnailJobRegistry record, @Param("example") SnailJobRegistryExample example);

    int updateByExample(@Param("record") SnailJobRegistry record, @Param("example") SnailJobRegistryExample example);

    int updateByPrimaryKeySelective(SnailJobRegistry record);

    int updateByPrimaryKey(SnailJobRegistry record);
}