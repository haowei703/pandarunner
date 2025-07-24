package com.shiroha.pandarunner.domain.entity;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import java.io.Serializable;
import java.sql.Timestamp;

import java.io.Serial;

import lombok.*;

/**
 * 评价回复表 实体类。
 *
 * @author haowei703
 * @since 2025-07-03
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("t_review_reply")
@EqualsAndHashCode(callSuper = true)
public class ReviewReply extends BaseEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 评价回复主键ID
     */
    @Id(keyType = KeyType.Auto)
    private Long id;

    /**
     * 关联的评价ID
     */
    private Long reviewId;

    /**
     * 回复内容
     */
    private String content;

}
