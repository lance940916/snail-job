package com.snailwu.job.core.biz.model;

/**
 * 触发任务参数
 *
 * @author 吴庆龙
 * @date 2020/5/25 12:05 下午
 */
public class TriggerParam {

    /**
     * 任务 ID
     */
    private Integer jobId;

    /**
     * 执行 handler、参数、堵塞策略、超时时间
     */
    private String execHandler;
    private String execParam;
    private String execBlockStrategy;
    private Integer execTimeout;

    /**
     * 任务对应的日志 ID 和 时间。用于进行回调，填充日志
     */
    private Long logId;

    public Integer getJobId() {
        return jobId;
    }

    public void setJobId(Integer jobId) {
        this.jobId = jobId;
    }

    public String getExecHandler() {
        return execHandler;
    }

    public void setExecHandler(String execHandler) {
        this.execHandler = execHandler;
    }

    public String getExecParam() {
        return execParam;
    }

    public void setExecParam(String execParam) {
        this.execParam = execParam;
    }

    public String getExecBlockStrategy() {
        return execBlockStrategy;
    }

    public void setExecBlockStrategy(String execBlockStrategy) {
        this.execBlockStrategy = execBlockStrategy;
    }

    public Integer getExecTimeout() {
        return execTimeout;
    }

    public void setExecTimeout(Integer execTimeout) {
        this.execTimeout = execTimeout;
    }

    public Long getLogId() {
        return logId;
    }

    public void setLogId(Long logId) {
        this.logId = logId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        TriggerParam that = (TriggerParam) o;
        if (!jobId.equals(that.jobId)) {
            return false;
        }
        return logId.equals(that.logId);
    }

    @Override
    public int hashCode() {
        int result = jobId.hashCode();
        result = 31 * result + logId.hashCode();
        return result;
    }

}
