package com.shiroha.pandarunner.domain.entity;

import com.mybatisflex.annotation.Column;
import lombok.Data;

import java.sql.Timestamp;

@Data
public abstract class BaseEntity {

    /**
     * 逻辑删除
     */
    @Column(isLogicDelete = true)
    private Boolean isDeleted = false;

    /**
     * 创建时间
     */
    @Column(onInsertValue = "CURRENT_TIMESTAMP")
    private Timestamp createdAt;

    /**
     * 更新时间
     */
    @Column(onUpdateValue = "CURRENT_TIMESTAMP")
    private Timestamp updatedAt;
}
