package com.shiroha.pandarunner.exception;

import lombok.Getter;

/**
 * 业务数据查询为空异常
 */
@Getter
public class BusinessDataNotFoundException extends BusinessException {

    public BusinessDataNotFoundException(String message, Object businessId) {
        super(message, businessId);
    }

    public BusinessDataNotFoundException(String message, Object businessId, Throwable cause) {
        super(message, businessId, cause);
    }
}
