package com.snailwu.job.core.response;

/**
 * @author 吴庆龙
 * @date 2020/5/22 3:07 下午
 */
public class RpcBaseResponse {

    public static final int SUCCESS_CODE = 200;
    public static final int FAIL_CODE = 500;

    public static final RpcBaseResponse SUCCESS = new RpcBaseResponse();
    public static final RpcBaseResponse FAIL = new RpcBaseResponse(FAIL_CODE, null);

    private int code;
    private String msg;

    public RpcBaseResponse() {
    }

    public RpcBaseResponse(int code, String msg) {
        this.code = code;
        this.msg = msg;
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

}
