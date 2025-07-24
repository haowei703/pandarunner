package com.shiroha.pandarunner.exception;

import lombok.Getter;

@Getter
public class InvalidParamException extends RuntimeException {
    // 错误参数
    private final String fieldName;
    // 异常类型
    private final ErrorType errorType;

    /**
     * 错误类型枚举
     */
    public enum ErrorType {
        NULL_VALUE,         // null值
        EMPTY_VALUE,        // 空值
        ILLEGAL_VALUE,      // 非法值
    }

    public InvalidParamException(String message, String fieldName, ErrorType errorType) {
        super(message);
        this.fieldName = fieldName;
        this.errorType = errorType;
    }

    public InvalidParamException(String message, String fieldName, ErrorType errorType, Throwable cause) {
        super(message, cause);
        this.fieldName = fieldName;
        this.errorType = errorType;
    }

    @Override
    public String toString() {
        return "InvalidParamException{" +
                "message=" + getMessage() +
                ", fieldName=" + fieldName +
                ", errorType=" + errorType +
                '}';
    }
}
