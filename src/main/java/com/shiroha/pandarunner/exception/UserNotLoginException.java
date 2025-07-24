package com.shiroha.pandarunner.exception;

import lombok.Getter;

@Getter
public class UserNotLoginException extends AuthException {
    public UserNotLoginException(String message) {
        super(message);
    }

    public UserNotLoginException(String message, Throwable cause) {
        super(message, cause);
    }
}
