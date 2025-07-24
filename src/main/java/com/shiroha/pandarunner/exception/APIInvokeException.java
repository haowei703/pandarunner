package com.shiroha.pandarunner.exception;

import lombok.Getter;

@Getter
public class APIInvokeException extends InternalServiceException {
    private Object body;
    private int code;

    public APIInvokeException(String message) {
        super(message);
    }

    public APIInvokeException(String message, Throwable cause) {
        super(message, cause);
    }

    public APIInvokeException(String message, int code) {
        super(message);
        this.code = code;
    }

    public APIInvokeException(String message, int code, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    public APIInvokeException(String message, int code, Object body) {
        super(message);
        this.code = code;
        this.body = body;
    }

    public APIInvokeException(String message, int code, Object body, Throwable cause) {
        super(message, cause);
        this.code = code;
        this.body = body;
    }
}
