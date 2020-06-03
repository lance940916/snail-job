package com.snailwu.job.core;

import com.snailwu.job.core.utils.JsonUtil;

/**
 * @author 吴庆龙
 * @date 2020/5/26 5:36 下午
 */
public class User {
    private int xVal;
    private String yVal;
    private int xxVal;

    public String getyVal() {
        return yVal;
    }

    public void setyVal(String yVal) {
        this.yVal = yVal;
    }

    public int getxVal() {
        return xVal;
    }

    public void setxVal(int xVal) {
        this.xVal = xVal;
    }

    public int getXxVal() {
        return xxVal;
    }

    public void setXxVal(int xxVal) {
        this.xxVal = xxVal;
    }

    public static void main(String[] args) {
        User user = new User();
        user.setxVal(111);
        user.setXxVal(222);
        user.setyVal("abc");
        System.out.println(JsonUtil.writeValueAsString(user));
    }
}
