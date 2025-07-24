package com.shiroha.pandarunner.exception;

import lombok.Getter;

/**
 * 业务异常
 *
 * @author haowei703
 * @since 2025-07-04
 */
@Getter
public class BusinessException extends RuntimeException {
    // 主业务ID
    private final Object businessId;

    public BusinessException(String message, Object businessId) {
        super(message);
        this.businessId = businessId;
    }

    public BusinessException(String message, Object businessId, Throwable cause) {
        super(message, cause);
        this.businessId = businessId;
    }

    @Override
    public String toString() {
        return "BusinessException{" +
                "message=" + getMessage() +
                ", businessId='" + businessId.toString() + '\'' +
                '}';
    }
}
