package com.snailwu.job.admin.controller.request;

/**
 * @author 吴庆龙
 * @date 2020/8/10 8:05 上午
 */
public class JobGroupSearchRequest extends BasePageRequest {

    private String title;
    private String appName;

    public String getTitle() {
        if (title == null || title.length() == 0) {
            return null;
        }
        return "%" + title + "%";
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAppName() {
        if (appName == null || appName.length() == 0) {
            return null;
        }
        return "%" + appName + "%";
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

}
