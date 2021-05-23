package com.snailwu.job.core.exception;

/**
 * @author 吴庆龙
 * @date 2020/7/23 2:34 下午
 */
public class JobException extends RuntimeException {

    public JobException() {
        super();
    }

    public JobException(String message) {
        super(message);
    }

    public JobException(String message, Throwable cause) {
        super(message, cause);
    }

    public JobException(Throwable cause) {
        super(cause);
    }

}
