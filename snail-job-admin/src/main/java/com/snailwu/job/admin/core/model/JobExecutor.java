package com.snailwu.job.admin.core.model;

import javax.annotation.Generated;
import java.util.Date;

public class JobExecutor {
    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private Integer id;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private String groupName;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private String address;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private Byte registryType;

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
    public String getGroupName() {
        return groupName;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public String getAddress() {
        return address;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setAddress(String address) {
        this.address = address;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public Byte getRegistryType() {
        return registryType;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setRegistryType(Byte registryType) {
        this.registryType = registryType;
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