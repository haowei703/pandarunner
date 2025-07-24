package com.shiroha.pandarunner.exception;

import lombok.Getter;

@Getter
public class CredentialsIncorrectException extends AuthException {
    public CredentialsIncorrectException(String message) {
        super(message);
    }
}
