package com.snailwu.job.admin.mapper;

import com.snailwu.job.admin.core.model.SnailJobInfo;
import com.snailwu.job.admin.core.model.SnailJobInfoExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface SnailJobInfoMapper {
    long countByExample(SnailJobInfoExample example);

    int deleteByExample(SnailJobInfoExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(SnailJobInfo record);

    int insertSelective(SnailJobInfo record);

    List<SnailJobInfo> selectByExampleWithBLOBs(SnailJobInfoExample example);

    List<SnailJobInfo> selectByExample(SnailJobInfoExample example);

    SnailJobInfo selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") SnailJobInfo record, @Param("example") SnailJobInfoExample example);

    int updateByExampleWithBLOBs(@Param("record") SnailJobInfo record, @Param("example") SnailJobInfoExample example);

    int updateByExample(@Param("record") SnailJobInfo record, @Param("example") SnailJobInfoExample example);

    int updateByPrimaryKeySelective(SnailJobInfo record);

    int updateByPrimaryKeyWithBLOBs(SnailJobInfo record);

    int updateByPrimaryKey(SnailJobInfo record);
}