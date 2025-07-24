package com.shiroha.pandarunner.exception;

import lombok.Getter;

@Getter
public class UserNotFoundException extends AuthException {
    public UserNotFoundException(String message) {
        super(message);
    }

    public UserNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
