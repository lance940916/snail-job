package com.snailwu.job.admin.request;

/**
 * @author 吴庆龙
 * @date 2020/8/10 8:05 上午
 */
public class JobGroupSearchRequest extends BasePageRequest {

    private String title;
    private String name;

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

}
