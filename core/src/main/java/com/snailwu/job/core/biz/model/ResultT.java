package com.snailwu.job.core.biz.model;

/**
 * Http 响应封装
 *
 * @author 吴庆龙
 * @date 2020/5/22 3:07 下午
 */
public class ResultT<T> {

    public static final int SUCCESS_CODE = 200;
    public static final int FAIL_CODE = 500;

    public static final ResultT<String> SUCCESS = new ResultT<>(null);
    public static final ResultT<String> FAIL = new ResultT<>(FAIL_CODE, null);

    private int code;
    private String msg;
    private T content;

    public ResultT() {
    }

    public ResultT(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public ResultT(T content) {
        this.code = SUCCESS_CODE;
        this.content = content;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getContent() {
        return content;
    }

    public void setContent(T content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "ResultT{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", content=" + content +
                '}';
    }
}
