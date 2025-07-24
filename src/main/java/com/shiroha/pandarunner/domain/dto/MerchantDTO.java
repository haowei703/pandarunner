package com.shiroha.pandarunner.domain.dto;

import com.shiroha.pandarunner.config.ValidationGroups;
import com.shiroha.pandarunner.domain.entity.Address;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * DTO for {@link com.shiroha.pandarunner.domain.entity.Merchant}
 */
@Data
@Schema(name = "MerchantDTO", description = "商家信息传输对象")
public class MerchantDTO implements Address, Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @NotBlank(message = "商家名称不能为空", groups = ValidationGroups.Create.class)
    @Size(max = 50, message = "商家名称长度不能超过50个字符",
            groups = {ValidationGroups.Create.class, ValidationGroups.Update.class})
    @Schema(description = "商家名称", requiredMode = Schema.RequiredMode.REQUIRED, maxLength = 50)
    private String name;

    @Size(max = 200, message = "商家描述长度不能超过200个字符",
            groups = {ValidationGroups.Create.class, ValidationGroups.Update.class})
    @Schema(description = "商家描述", maxLength = 200)
    private String description;

    @NotBlank(message = "商家Logo不能为空", groups = ValidationGroups.Create.class)
    @Pattern(regexp = "^(?:https?|ftp)://[^\\s/$.?#]\\S*|^/\\S*",
            message = "头像URL格式不正确：必须是完整的URL（以http://、https://或ftp://开头）或相对路径（以/开头）")
    @Schema(description = "商家Logo图片URL", requiredMode = Schema.RequiredMode.REQUIRED,
            pattern = "^(?:https?|ftp)://[^\\s/$.?#]\\S*|^/\\S*")
    private String logo;

    @NotBlank(message = "联系电话不能为空", groups = ValidationGroups.Create.class)
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确",
            groups = {ValidationGroups.Create.class, ValidationGroups.Update.class})
    @Schema(description = "商家联系电话", requiredMode = Schema.RequiredMode.REQUIRED,
            pattern = "^1[3-9]\\d{9}$")
    private String contactPhone;

    // 创建时必需，更新时忽略（不添加Update分组）
    @NotBlank(message = "省份不能为空", groups = ValidationGroups.Create.class)
    @Size(max = 20, message = "省份名称长度不能超过20个字符", groups = ValidationGroups.Create.class)
    @Schema(description = "省份", requiredMode = Schema.RequiredMode.REQUIRED, maxLength = 20)
    private String province;

    // 创建时必需，更新时忽略
    @NotBlank(message = "城市不能为空", groups = ValidationGroups.Create.class)
    @Size(max = 20, message = "城市名称长度不能超过20个字符", groups = ValidationGroups.Create.class)
    @Schema(description = "城市", requiredMode = Schema.RequiredMode.REQUIRED, maxLength = 20)
    private String city;

    // 创建时必需，更新时忽略
    @NotBlank(message = "地区不能为空", groups = ValidationGroups.Create.class)
    @Size(max = 20, message = "地区名称长度不能超过20个字符", groups = ValidationGroups.Create.class)
    @Schema(description = "地区", requiredMode = Schema.RequiredMode.REQUIRED, maxLength = 20)
    private String district;

    // 创建时必需，更新时忽略
    @NotBlank(message = "详细地址不能为空", groups = ValidationGroups.Create.class)
    @Size(max = 100, message = "详细地址长度不能超过100个字符", groups = ValidationGroups.Create.class)
    @Schema(description = "商家详细地址", requiredMode = Schema.RequiredMode.REQUIRED, maxLength = 100)
    private String detailAddress;

    // 创建时必需，更新时忽略
    @NotBlank(message = "营业执照编号不能为空", groups = ValidationGroups.Create.class)
    @Size(max = 30, message = "营业执照编号长度不能超过30个字符", groups = ValidationGroups.Create.class)
    @Schema(description = "营业执照编号", requiredMode = Schema.RequiredMode.REQUIRED, maxLength = 30)
    private String businessLicense;

    @NotNull(message = "最低起送金额不能为空", groups = ValidationGroups.Create.class)
    @DecimalMin(value = "0.00", message = "最低起送金额不能为负数",
            groups = {ValidationGroups.Create.class, ValidationGroups.Update.class})
    @Schema(description = "最低起送金额", requiredMode = Schema.RequiredMode.REQUIRED, minimum = "0.00")
    private BigDecimal minOrderAmount;

    @NotNull(message = "配送费不能为空", groups = ValidationGroups.Create.class)
    @DecimalMin(value = "0.00", message = "配送费不能为负数",
            groups = {ValidationGroups.Create.class, ValidationGroups.Update.class})
    @Schema(description = "配送费", requiredMode = Schema.RequiredMode.REQUIRED, minimum = "0.00")
    private BigDecimal deliveryFee;

    @NotNull(message = "打包费不能为空", groups = ValidationGroups.Create.class)
    @DecimalMin(value = "0.00", message = "打包费不能为负数",
            groups = {ValidationGroups.Create.class, ValidationGroups.Update.class})
    @Schema(description = "打包费", requiredMode = Schema.RequiredMode.REQUIRED, minimum = "0.00")
    private BigDecimal packingFee;

}