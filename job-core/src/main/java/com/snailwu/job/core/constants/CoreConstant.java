package com.snailwu.job.core.constants;

/**
 * @author 吴庆龙
 * @date 2020/11/27 上午10:55
 */
public interface CoreConstant {

    String ACCESS_TOKEN = "access-token";

    /**
     * URL 中的分隔符
     */
    String URL_SEPARATOR = "/";

    /**
     * 心跳间隔时间
     * 单位：秒
     */
    int BEAT_TIME = 10;

    /**
     * 主机死亡时间
     * 规定 心跳三次失败，则认定主机死亡
     * 单位：秒
     */
    int DEAD_TIME = BEAT_TIME * 3;

    /**
     * 整理节点机器地址的时间间隔
     */
    int SORT_NODE_ADDRESS_TIME = 10;

}
