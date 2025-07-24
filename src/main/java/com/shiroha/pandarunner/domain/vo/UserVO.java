package com.shiroha.pandarunner.domain.vo;

import java.io.Serial;
import java.io.Serializable;

import cn.dev33.satoken.stp.SaTokenInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.shiroha.pandarunner.domain.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
@Schema(description = "用户VO")
public class UserVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
    * 用户ID
    */
    @NotNull(message="用户ID不能为空")
    @Schema(description = "用户ID")
    private Long id;

    /**
    * 用户名
    */
    @NotBlank(message="用户名不能为空")
    @Size(max= 50, message="编码长度不能超过50")
    @Schema(description ="用户名")
    private String username;

    /**
    * 电话
    */
    @NotBlank(message="电话不能为空")
    @Size(min = 11, max = 11, message="电话长度必须是11位")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "电话格式错误")
    @Schema(description = "电话")
    private String phone;

    /**
    * 头像URL
    */
    @Size(max= 255, message="编码长度不能超过255")
    @Schema(description = "头像URL")
    @Pattern(regexp = "^(https?|ftp)://[^\\s/$.?#].\\S*$", message = "头像URL格式不正确")
    private String avatar;

    /**
    * 性别(0-未知 1-男 2-女)
    */
    @Schema(description = "性别(0-未知 1-男 2-女)")
    @Size(max = 2, message = "性别代码不能大于2")
    private User.Gender gender;
}
