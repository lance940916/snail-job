package com.snailwu.job.admin.core.model;

import java.util.Date;
import javax.annotation.Generated;

public class SnailJobRegistry {
    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private Integer id;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private String registryGroup;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private String registryKey;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private String registryValue;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private Date updateTime;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public Integer getId() {
        return id;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setId(Integer id) {
        this.id = id;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public String getRegistryGroup() {
        return registryGroup;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setRegistryGroup(String registryGroup) {
        this.registryGroup = registryGroup;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public String getRegistryKey() {
        return registryKey;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setRegistryKey(String registryKey) {
        this.registryKey = registryKey;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public String getRegistryValue() {
        return registryValue;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setRegistryValue(String registryValue) {
        this.registryValue = registryValue;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public Date getUpdateTime() {
        return updateTime;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}