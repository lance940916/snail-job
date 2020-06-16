package com.snailwu.job.admin.core.model;

import javax.annotation.Generated;

public class JobLock {
    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private String lockName;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public String getLockName() {
        return lockName;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setLockName(String lockName) {
        this.lockName = lockName;
    }
}