package com.snailwu.job.core.biz.model;

/**
 * @author 吴庆龙
 * @date 2020/5/25 2:39 下午
 */
public class CallbackParam {

    private long logId;
    private long logDateTime;
    private ResultT<String> executeResult;

    public CallbackParam() {
    }

    public CallbackParam(long logId, long logDateTime, ResultT<String> executeResult) {
        this.logId = logId;
        this.logDateTime = logDateTime;
        this.executeResult = executeResult;
    }

    public long getLogId() {
        return logId;
    }

    public void setLogId(long logId) {
        this.logId = logId;
    }

    public long getLogDateTime() {
        return logDateTime;
    }

    public void setLogDateTime(long logDateTime) {
        this.logDateTime = logDateTime;
    }

    public ResultT<String> getExecuteResult() {
        return executeResult;
    }

    public void setExecuteResult(ResultT<String> executeResult) {
        this.executeResult = executeResult;
    }
}
