package com.snailwu.job.core.exception;

/**
 * @author 吴庆龙
 * @date 2020/11/27 下午3:28
 */
public class JobHttpException extends RuntimeException {
    public JobHttpException() {
    }

    public JobHttpException(String message) {
        super(message);
    }

    public JobHttpException(String message, Throwable cause) {
        super(message, cause);
    }

    public JobHttpException(Throwable cause) {
        super(cause);
    }

    public JobHttpException(String message, Throwable cause, boolean enableSuppression,
                            boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
