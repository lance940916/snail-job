package com.snailwu.job.core.exception;

/**
 * @author 吴庆龙
 * @date 2020/7/23 2:34 下午
 */
public class SnailJobException extends RuntimeException {

    public SnailJobException() {
        super();
    }

    public SnailJobException(String message) {
        super(message);
    }

    public SnailJobException(String message, Throwable cause) {
        super(message, cause);
    }

    public SnailJobException(Throwable cause) {
        super(cause);
    }

}
