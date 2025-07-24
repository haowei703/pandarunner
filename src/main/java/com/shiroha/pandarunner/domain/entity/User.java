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
 * 用户表 实体类。
 *
 * @author haowei703
 * @since 2025-07-03
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("t_user")
@EqualsAndHashCode(callSuper = true)
public class User extends BaseEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    @Id(keyType = KeyType.Auto)
    private Long id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 电话
     */
    private String phone;

    /**
     * 头像URL
     */
    private String avatar;

    /**
     * 性别(0-未知 1-男 2-女)
     */
    private Gender gender;

    /**
     * 角色(0-普通用户 1-管理员 2-超级管理员)
     */
    private Role role;

    /**
     * 状态(0-禁用 1-正常)
     */
    private UserStatus status;

    @Getter
    public enum Gender {
        /**
         * 未知
         */
        UNKNOWN(0, "未知"),

        /**
         * 男
         */
        MALE(1, "男"),

        /**
         * 女
         */
        FEMALE(2, "女");

        @EnumValue
        private final int code;

        @JsonValue
        private final String description;

        Gender(int code, String description) {
            this.code = code;
            this.description = description;
        }
    }

    @Getter
    public enum UserStatus {
        /**
         * 正常
         */
        NORMAL(1, "正常"),

        /**
         * 禁用
         */
        DISABLED(2, "禁用");

        @EnumValue
        private final int code;

        @JsonValue
        private final String description;

        UserStatus(int code, String description) {
            this.code = code;
            this.description = description;
        }
    }

    @Getter
    public enum Role {
        /**
         * 普通用户
         */
        USER(0, "普通用户"),

        /**
         * 管理员
         */
        ADMIN(1, "管理员"),

        /**
         * 超级管理员
         */
        SUPER_ADMIN(2, "超级管理员");


        @EnumValue
        private final int code;

        @JsonValue
        private final String description;

        Role(int code, String description) {
            this.code = code;
            this.description = description;
        }

    }
}
