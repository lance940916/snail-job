package com.snailwu.job.admin.controller.request;

/**
 * @author 吴庆龙
 * @date 2020/8/14 10:16 上午
 */
public class BasePageRequest {

    private Integer page;
    private Integer limit;

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

}
