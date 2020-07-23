package com.snailwu.job.core.exception;

/**
 * @author 吴庆龙
 * @date 2020/7/23 2:34 下午
 */
public class JonRuntimeException extends RuntimeException {

    public JonRuntimeException() {
        super();
    }

    public JonRuntimeException(String message) {
        super(message);
    }

    public JonRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public JonRuntimeException(Throwable cause) {
        super(cause);
    }

}
