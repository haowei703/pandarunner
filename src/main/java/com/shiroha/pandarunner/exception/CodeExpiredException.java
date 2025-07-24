package com.shiroha.pandarunner.exception;

import lombok.Getter;

@Getter
public class CodeExpiredException extends AuthException {

    public CodeExpiredException(String message) {
        super(message);
    }

    public CodeExpiredException(String message, Throwable cause) {
        super(message, cause);
    }
}
