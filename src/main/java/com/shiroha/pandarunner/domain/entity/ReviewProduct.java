package com.shiroha.pandarunner.domain.entity;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import java.io.Serializable;
import java.sql.Array;

import java.io.Serial;

import lombok.*;

/**
 * 商品评价表 实体类。
 *
 * @author haowei703
 * @since 2025-07-03
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("t_review_product")
@EqualsAndHashCode(callSuper = true)
public class ReviewProduct extends BaseEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 商品评价主键ID
     */
    @Id(keyType = KeyType.Auto)
    private Long id;

    /**
     * 关联的评价主表ID
     */
    private Long reviewId;

    /**
     * 评价的商品ID
     */
    private Long productId;

    /**
     * 评分(0-5)
     */
    private Integer rating;

    /**
     * 评价内容
     */
    private String content;

    /**
     * 评价图片列表
     */
    private Array imageUrls;

}
