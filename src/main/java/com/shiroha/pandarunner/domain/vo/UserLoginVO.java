package com.shiroha.pandarunner.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserLoginVO {

    /**
     * 用户名
     */
    @NotBlank(message="[用户名]不能为空")
    @Size(max= 50, message="编码长度不能超过50")
    @Schema(description ="用户名")
    private String username;

    /**
     * 密码
     */
    @NotBlank(message="[密码]不能为空")
    @Size(max= 100, message="编码长度不能超过100")
    @Schema(description = "密码")
    private String password;
}
