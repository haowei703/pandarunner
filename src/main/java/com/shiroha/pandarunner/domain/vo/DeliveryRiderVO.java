package com.shiroha.pandarunner.domain.vo;

import com.shiroha.pandarunner.domain.entity.DeliveryRider;
import lombok.Data;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * 骑手信息 VO
 *
 * @author haowei703
 * @since 2025-07-03
 */
@Data
public class DeliveryRiderVO {

    /**
     * 骑手主键ID
     */
    private Long id;

    /**
     * 关联的用户ID
     */
    private Long userId;

    /**
     * 骑手姓名
     */
    private String name;

    /**
     * 骑手电话
     */
    private String phone;

    /**
     * 状态
     */
    private DeliveryRider.DeliveryRiderStatus status;

    /**
     * 骑手位置
     */
    private String currentLocation;

    /**
     * 骑手评分(1.0-5.0)
     */
    private BigDecimal rating;

    /**
     * 累计配送单数
     */
    private Integer deliveryCount;

    /**
     * 创建时间
     */
    private Timestamp createdAt;
}