package com.snailwu.job.admin.core.route;

import com.snailwu.job.admin.core.route.strategy.*;
import com.snailwu.job.admin.utils.I18nUtil;

/**
 * 任务路由策略
 *
 * @author 吴庆龙
 * @date 2020/6/4 11:23 上午
 */
public enum ExecutorRouteStrategyEnum {

    FIRST(I18nUtil.getString("jobconf_route_first"), new ExecutorRouteFirst()),
    LAST(I18nUtil.getString("jobconf_route_last"), new ExecutorRouteLast()),
    ROUND(I18nUtil.getString("jobconf_route_round"), new ExecutorRouteRound()),
    RANDOM(I18nUtil.getString("jobconf_route_random"), new ExecutorRouteRandom()),
    CONSISTENT_HASH(I18nUtil.getString("jobconf_route_consistenthash"), new ExecutorRouteConsistentHash()),
    LEAST_FREQUENTLY_USED(I18nUtil.getString("jobconf_route_lfu"), new ExecutorRouteLFU()),
    LEAST_RECENTLY_USED(I18nUtil.getString("jobconf_route_lru"), new ExecutorRouteLRU()),
    FAILOVER(I18nUtil.getString("jobconf_route_failover"), new ExecutorRouteFailOver()),
    BUSYOVER(I18nUtil.getString("jobconf_route_busyover"), new ExecutorRouteBusyOver()),
    SHARDING_BROADCAST(I18nUtil.getString("jobconf_route_shard"), null);

    ExecutorRouteStrategyEnum(String title, ExecutorRouter router) {
        this.title = title;
        this.router = router;
    }

    private final String title;
    private final ExecutorRouter router;

    public String getTitle() {
        return title;
    }

    public ExecutorRouter getRouter() {
        return router;
    }

    public static ExecutorRouteStrategyEnum match(String name, ExecutorRouteStrategyEnum defaultItem) {
        if (name != null) {
            for (ExecutorRouteStrategyEnum item : ExecutorRouteStrategyEnum.values()) {
                if (item.name().equals(name)) {
                    return item;
                }
            }
        }
        return defaultItem;
    }

}
