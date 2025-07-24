package com.shiroha.pandarunner.domain.entity;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import java.io.Serializable;
import java.sql.Timestamp;

import java.io.Serial;

import lombok.*;

/**
 * 评价表 实体类。
 *
 * @author haowei703
 * @since 2025-07-03
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("t_review")
@EqualsAndHashCode(callSuper = true)
public class Review extends BaseEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 评价主键ID
     */
    @Id(keyType = KeyType.Auto)
    private Long id;

    /**
     * 评价用户ID
     */
    private Long userId;

    /**
     * 关联的订单ID
     */
    private Long orderId;

    /**
     * 关联的商家ID
     */
    private Long merchantId;

    /**
     * 商家评分
     */
    private Integer merchantRating;

    /**
     * 配送评分
     */
    private Integer deliveryRating;

    /**
     * 评价内容
     */
    private String content;

}
