package com.snailwu.job.core.biz.model;

/**
 * 终止任务参数
 *
 * @author 吴庆龙
 * @date 2020/5/25 12:59 下午
 */
public class KillParam {

    /**
     * 任务ID
     */
    private int jobId;

    /**
     * 任务对应的日志ID
     */
    private long logId;

    public KillParam() {
    }

    public KillParam(int jobId, long logId) {
        this.jobId = jobId;
        this.logId = logId;
    }

    public int getJobId() {
        return jobId;
    }

    public void setJobId(int jobId) {
        this.jobId = jobId;
    }

    public long getLogId() {
        return logId;
    }

    public void setLogId(long logId) {
        this.logId = logId;
    }

    public TriggerParam convertTriggerParam() {
        TriggerParam param = new TriggerParam();
        param.setJobId(this.jobId);
        param.setLogId(this.logId);
        return param;
    }

}
