package com.snailwu.job.core.enums;

/**
 * @author 吴庆龙
 * @date 2020/5/25 4:16 下午
 */
public class RegistryConfig {

    public static final int BEAT_TIMEOUT = 30;
    public static final int DEAD_TIMEOUT = BEAT_TIMEOUT * 3;

    public enum RegistryType {
        EXECUTOR, ADMIN
    }

}
