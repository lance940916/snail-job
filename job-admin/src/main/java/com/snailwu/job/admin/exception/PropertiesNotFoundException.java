package com.snailwu.job.admin.exception;

/**
 * 属性未找到异常
 *
 * @author 吴庆龙
 * @date 2020/11/25 下午5:30
 */
public class PropertiesNotFoundException extends RuntimeException {

    public PropertiesNotFoundException() {
    }

    public PropertiesNotFoundException(String message) {
        super(message);
    }

    public PropertiesNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public PropertiesNotFoundException(Throwable cause) {
        super(cause);
    }

    public PropertiesNotFoundException(String message, Throwable cause, boolean enableSuppression,
                                       boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
