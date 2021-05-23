package com.snailwu.job.core.exception;

/**
 * @author 吴庆龙
 * @date 2020/11/27 下午2:43
 */
public class JobJsonException extends RuntimeException {
    public JobJsonException() {
    }

    public JobJsonException(String message) {
        super(message);
    }

    public JobJsonException(String message, Throwable cause) {
        super(message, cause);
    }

    public JobJsonException(Throwable cause) {
        super(cause);
    }

    public JobJsonException(String message, Throwable cause, boolean enableSuppression,
                            boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
