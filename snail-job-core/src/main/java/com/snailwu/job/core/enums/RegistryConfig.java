package com.snailwu.job.core.enums;

/**
 * 注册配置
 *
 * @author 吴庆龙
 * @date 2020/5/25 4:16 下午
 */
public class RegistryConfig {

    // 心跳超时时间
    public static final int BEAT_TIMEOUT = 30; // 秒

    // 主机死亡时间
    public static final int DEAD_TIMEOUT = BEAT_TIMEOUT * 3; // 秒

}
