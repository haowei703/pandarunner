package com.shiroha.pandarunner.exception;

import lombok.Getter;

@Getter
public class InternalServiceException extends RuntimeException {
    public InternalServiceException(String message) {
        super(message);
    }

    public InternalServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
