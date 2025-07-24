package com.shiroha.pandarunner.domain.entity;

import com.fasterxml.jackson.annotation.JsonValue;
import com.mybatisflex.annotation.EnumValue;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;

/**
 * 系统公告表 实体类。
 *
 * @author haowei703
 * @since 2025-07-03
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("t_announcement")
@EqualsAndHashCode(callSuper = true)
public class Announcement extends BaseEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 公告主键ID
     */
    @Id(keyType = KeyType.Auto)
    private Long id;

    /**
     * 公告标题
     */
    private String title;

    /**
     * 公告内容
     */
    private String content;

    /**
     * 公告发布者ID
     */
    private Long publisherId;

    /**
     * 公告状态(0-停用 1-启用)
     */
    private AnnouncementStatus status;

    @Getter
    public enum AnnouncementStatus {
        /**
         * 停用
         */
        INACTIVE(0, "停用"),

        /**
         * 启用
         */
        ACTIVE(1, "启用");

        @EnumValue
        private final int code;

        @JsonValue
        private final String description;

        AnnouncementStatus(int code, String description) {
            this.code = code;
            this.description = description;
        }
    }

}
