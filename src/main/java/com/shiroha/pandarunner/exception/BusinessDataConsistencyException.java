package com.shiroha.pandarunner.exception;

import lombok.Getter;

/**
 * 业务数据一致性异常
 * 当业务数据关联关系不满足要求时抛出，例如：
 * 1. 根据ID查询不到对应的数据
 * 2. 数据关联关系不匹配(如订单商品不属于指定商家)
 *
 * @author haowei703
 * @since 2025-07-04
 */
@Getter
public class BusinessDataConsistencyException extends BusinessException {
    // 关联业务ID
    private final Object relatedId;
    // 异常类型
    private final ErrorType errorType;

    /**
     * 错误类型枚举
     */
    public enum ErrorType {
        DATA_NOT_FOUND,          // 数据不存在
        RELATION_MISMATCH,       // 关联关系不匹配
        INVALID_BUSINESS_STATE   // 业务状态无效
    }

    public BusinessDataConsistencyException(String message, Object businessId, Object relatedId, ErrorType errorType) {
        super(message, businessId);
        this.relatedId = relatedId;
        this.errorType = errorType;
    }

    public BusinessDataConsistencyException(String message, Object businessId, Object relatedId, ErrorType errorType, Throwable cause) {
        super(message, businessId, cause);
        this.relatedId = relatedId;
        this.errorType = errorType;
    }

    @Override
    public String toString() {
        return "BusinessDataConsistencyException{" +
                "message=" + getMessage() +
                ", businessId='" + super.getBusinessId().toString() + '\'' +
                ", relatedId='" + relatedId.toString() + '\'' +
                ", errorType=" + errorType +
                '}';
    }
}
