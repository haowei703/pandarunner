package com.shiroha.pandarunner.exception;

import lombok.Getter;

@Getter
public class UserNotPermissionException extends AuthException {

    public UserNotPermissionException(String message) {
        super(message);
    }

    public UserNotPermissionException(String message, Throwable cause) {
        super(message, cause);
    }
}
