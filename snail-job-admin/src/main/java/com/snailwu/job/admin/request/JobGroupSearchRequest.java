package com.snailwu.job.admin.request;

/**
 * @author 吴庆龙
 * @date 2020/8/10 8:05 上午
 */
public class JobGroupSearchRequest {

    private String title;
    private String name;
    private Integer page;
    private Integer limit;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

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
