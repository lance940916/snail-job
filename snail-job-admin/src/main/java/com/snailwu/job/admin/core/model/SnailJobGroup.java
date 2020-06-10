package com.snailwu.job.admin.core.model;

import javax.annotation.Generated;

public class SnailJobGroup {
    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private Integer id;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private String appName;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private String title;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private Byte addressType;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private String addressList;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public Integer getId() {
        return id;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setId(Integer id) {
        this.id = id;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public String getAppName() {
        return appName;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setAppName(String appName) {
        this.appName = appName;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public String getTitle() {
        return title;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setTitle(String title) {
        this.title = title;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public Byte getAddressType() {
        return addressType;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setAddressType(Byte addressType) {
        this.addressType = addressType;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public String getAddressList() {
        return addressList;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setAddressList(String addressList) {
        this.addressList = addressList;
    }
}