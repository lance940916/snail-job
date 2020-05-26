package com.snailwu.job.core.enums;

/**
 * @author 吴庆龙
 * @date 2020/5/26 2:48 下午
 */
public enum ExecutorBlockStrategyEnum {

    SERIAL_EXECUTION("Serial execution"),
    /*CONCURRENT_EXECUTION("并行"),*/
    DISCARD_LATER("Discard Later"),
    COVER_EARLY("Cover Early");

    private String title;

    ExecutorBlockStrategyEnum(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public static ExecutorBlockStrategyEnum match(String name, ExecutorBlockStrategyEnum defaultEnum) {
        if (name != null) {
            for (ExecutorBlockStrategyEnum item : ExecutorBlockStrategyEnum.values()) {
                if (item.name().equals(name)) {
                    return item;
                }
            }
        }
        return defaultEnum;
    }

}
