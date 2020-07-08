package com.snailwu.job.admin.trigger;

/**
 * 任务触发类型
 *
 * @author 吴庆龙
 * @date 2020/6/17 1:59 下午
 */
public enum TriggerTypeEnum {

    MANUAL("manual", (byte) 1, "手动触发"),
    CRON("cron", (byte) 2, "Cron触发"),
    RETRY("retry", (byte) 3, "失败重试触发"),
    API("api", (byte) 4, "API触发"),
    ;

    private final String name;
    private final Byte value;
    private final String desc;

    TriggerTypeEnum(String name, Byte value, String desc) {
        this.name = name;
        this.value = value;
        this.desc = desc;
    }

    public String getName() {
        return name;
    }

    public Byte getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }
}
