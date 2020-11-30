package com.snailwu.job.core.constants;

/**
 * 注册配置
 *
 * @author 吴庆龙
 * @date 2020/5/25 4:16 下午
 */
public class RegistryConstant {

    /**
     * 心跳间隔时间
     * 单位：秒
     */
    public static final int BEAT_TIME = 30;

    /**
     * 主机死亡时间
     * 规定 心跳三次失败，则认定主机死亡
     * 单位：秒
     */
    public static final int DEAD_TIME = BEAT_TIME * 3;

}
