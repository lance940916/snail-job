package com.snailwu.job.core.enums;

/**
 * 执行器堵塞策略
 *
 * @author 吴庆龙
 * @date 2020/6/22 11:08 上午
 */
public enum ExecutorBlockStrategyEnum {

    SERIAL_EXECUTION("Serial execution", (byte) 1, "单机串行"),
    DISCARD_LATER("Discard Later", (byte) 2, "丢弃后续调度"),
    COVER_EARLY("Cover Early", (byte) 3, "覆盖之前调度");

    private final String name;
    private final Byte value;
    private final String desc;

    ExecutorBlockStrategyEnum(String name, Byte value, String desc) {
        this.name = name;
        this.value = value;
        this.desc = desc;
    }

    public String getName() {
        return this.name;
    }

    public Byte getValue() {
        return this.value;
    }

    public String getDesc() {
        return this.desc;
    }

    public static ExecutorBlockStrategyEnum match(String name, ExecutorBlockStrategyEnum defaultItem) {
        if (name != null) {
            for (ExecutorBlockStrategyEnum item : ExecutorBlockStrategyEnum.values()) {
                if (item.name().equals(name)) {
                    return item;
                }
            }
        }
        return defaultItem;
    }

}
