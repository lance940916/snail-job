package com.snailwu.job.admin.core.model;

import javax.annotation.Generated;

public class JobGroup {
    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private Integer id;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private String title;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private String name;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private String type;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private String addressList;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private String description;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public Integer getId() {
        return id;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setId(Integer id) {
        this.id = id;
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
    public String getName() {
        return name;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setName(String name) {
        this.name = name;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public String getType() {
        return type;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setType(String type) {
        this.type = type;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public String getAddressList() {
        return addressList;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setAddressList(String addressList) {
        this.addressList = addressList;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public String getDescription() {
        return description;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setDescription(String description) {
        this.description = description;
    }
}