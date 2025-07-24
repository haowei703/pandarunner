package com.shiroha.pandarunner.exception;

import lombok.Getter;

/**
 * 不支持的操作异常
 *
 * @author haowei703
 * @since 2025-07-04
 */
@Getter
public class NotSupportException extends RuntimeException {
    public NotSupportException(String message) {
        super(message);
    }
}
