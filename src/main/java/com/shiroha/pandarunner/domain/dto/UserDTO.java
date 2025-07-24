package com.shiroha.pandarunner.domain.dto;

import com.shiroha.pandarunner.config.ValidationGroups;
import com.shiroha.pandarunner.domain.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "用户DTO")
public class UserDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 用户名
     */
    @NotBlank(message = "用户名不能为空", groups = ValidationGroups.Create.class)
    @Size(max = 50, message = "用户名长度不能超过50", groups = ValidationGroups.Create.class)
    @Schema(description = "用户名", requiredMode = Schema.RequiredMode.REQUIRED)
    private String username;

    /**
     * 密码
     */
    @NotBlank(message = "密码不能为空", groups = ValidationGroups.Create.class)
    @Size(max = 100, message = "密码长度不能超过100", groups = ValidationGroups.Create.class)
    @Schema(description = "密码", requiredMode = Schema.RequiredMode.REQUIRED)
    private String password;

    /**
     * 电话
     */
    @NotBlank(message = "电话不能为空", groups = ValidationGroups.Create.class)
    @Size(min = 11, max = 11, message = "电话长度必须是11位", groups = ValidationGroups.Create.class)
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "电话格式错误", groups = ValidationGroups.Create.class)
    @Schema(description = "电话", requiredMode = Schema.RequiredMode.REQUIRED)
    private String phone;

    /**
     * 验证码
     */
    @NotBlank(message = "验证码不能为空", groups = ValidationGroups.Create.class)
    @Size(min = 6, max = 6, message = "验证码长度为6位", groups = ValidationGroups.Create.class)
    @Schema(description = "验证码", requiredMode = Schema.RequiredMode.REQUIRED)
    private String code;

    /**
     * 头像URL
     */
    @Size(max = 255, message = "头像URL长度不能超过255",
            groups = {ValidationGroups.Create.class, ValidationGroups.Update.class})
    @Pattern(regexp = "^(?:https?|ftp)://[^\\s/$.?#]\\S*|^/\\S*",
            message = "头像URL格式不正确：必须是完整的URL（以http://、https://或ftp://开头）或相对路径（以/开头）",
            groups = {ValidationGroups.Create.class, ValidationGroups.Update.class})
    @Schema(description = "头像URL", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String avatar;

    /**
     * 性别(0-未知 1-男 2-女)
     */
    @Schema(description = "性别(0-未知 1-男 2-女)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private User.Gender gender;
}