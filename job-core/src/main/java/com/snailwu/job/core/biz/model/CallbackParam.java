package com.snailwu.job.core.biz.model;

import java.util.Date;

/**
 * 回调任务执行结果
 *
 * @author 吴庆龙
 * @date 2020/5/25 2:39 下午
 */
public class CallbackParam {

    private long logId;
    private Date execTime;
    private int execCode;
    private String execMsg;

    public CallbackParam() {
    }

    public CallbackParam(long logId, Date execTime, int execCode, String execMsg) {
        this.logId = logId;
        this.execTime = execTime;
        this.execCode = execCode;
        this.execMsg = execMsg;
    }

    public long getLogId() {
        return logId;
    }

    public void setLogId(long logId) {
        this.logId = logId;
    }

    public Date getExecTime() {
        return execTime;
    }

    public void setExecTime(Date execTime) {
        this.execTime = execTime;
    }

    public int getExecCode() {
        return execCode;
    }

    public void setExecCode(int execCode) {
        this.execCode = execCode;
    }

    public String getExecMsg() {
        return execMsg;
    }

    public void setExecMsg(String execMsg) {
        this.execMsg = execMsg;
    }
}
