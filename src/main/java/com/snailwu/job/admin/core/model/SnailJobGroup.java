package com.snailwu.job.admin.core.model;

public class SnailJobGroup {
    private Integer id;

    private String appName;

    private String title;

    private Byte addressType;

    private String addressList;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Byte getAddressType() {
        return addressType;
    }

    public void setAddressType(Byte addressType) {
        this.addressType = addressType;
    }

    public String getAddressList() {
        return addressList;
    }

    public void setAddressList(String addressList) {
        this.addressList = addressList;
    }
}