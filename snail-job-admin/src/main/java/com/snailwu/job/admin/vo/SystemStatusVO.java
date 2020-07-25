package com.snailwu.job.admin.vo;

/**
 * @author 吴庆龙
 * @date 2020/7/25 10:19
 */
public class SystemStatusVO {

    // 总任务数量
    private Integer totalJobAmount;

    // 总执行器数量
    private Integer totalGroupAmount;

    // 调度次数
    private Integer totalInvokeTimes;

    public Integer getTotalJobAmount() {
        return totalJobAmount;
    }

    public void setTotalJobAmount(Integer totalJobAmount) {
        this.totalJobAmount = totalJobAmount;
    }

    public Integer getTotalGroupAmount() {
        return totalGroupAmount;
    }

    public void setTotalGroupAmount(Integer totalGroupAmount) {
        this.totalGroupAmount = totalGroupAmount;
    }

    public Integer getTotalInvokeTimes() {
        return totalInvokeTimes;
    }

    public void setTotalInvokeTimes(Integer totalInvokeTimes) {
        this.totalInvokeTimes = totalInvokeTimes;
    }
}
