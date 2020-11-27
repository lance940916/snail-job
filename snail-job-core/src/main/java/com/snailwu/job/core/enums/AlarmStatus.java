package com.snailwu.job.core.enums;

/**
 * 执行器注册类型
 *
 * @author 吴庆龙
 * @date 2020/6/30 2:31 下午
 */
public enum AlarmStatus {

    LOCK("lock", (byte) -1, "锁定状态"),
    DEFAULT("auto", (byte) 0, "默认"),
    NOT_ALARM("not_alarm", (byte) 1, "无需告警"),
    ALARM_SUCCESS("alarm_success", (byte) 2, "告警成功"),
    ALARM_FAIL("alarm_fail", (byte) 3, "告警失败"),
    ;

    private final String name;
    private final Byte value;
    private final String desc;

    AlarmStatus(String name, Byte value, String desc) {
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
