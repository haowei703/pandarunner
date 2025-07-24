package com.shiroha.pandarunner.domain.dto;

import com.shiroha.pandarunner.config.ValidationGroups;
import com.shiroha.pandarunner.domain.entity.Address;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "用户地址DTO")
public class UserAddressDTO implements Address, Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 收货人姓名
     */
    @NotNull(message = "收货人姓名不能为空", groups = ValidationGroups.Create.class)
    @Size(max = 20, message = "收货人姓名长度不能超过20", groups = ValidationGroups.Create.class)
    @Schema(description = "收货人姓名", requiredMode = Schema.RequiredMode.REQUIRED)
    private String receiverName;

    /**
     * 收货人电话
     */
    @NotBlank(message = "电话不能为空", groups = ValidationGroups.Create.class)
    @Size(min = 11, max = 11, message = "电话长度必须是11位", groups = ValidationGroups.Create.class)
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "电话格式错误", groups = ValidationGroups.Create.class)
    @Schema(description = "收货人电话", requiredMode = Schema.RequiredMode.REQUIRED)
    private String receiverPhone;

    /**
     * 收货地址省份
     */
    @NotNull(message = "收货地址省份不能为空", groups = ValidationGroups.Create.class)
    @Schema(description = "收货地址省份", requiredMode = Schema.RequiredMode.REQUIRED)
    private String province;

    /**
     * 收货地址城市
     */
    @NotNull(message = "收货地址城市不能为空", groups = ValidationGroups.Create.class)
    @Schema(description = "收货地址城市", requiredMode = Schema.RequiredMode.REQUIRED)
    private String city;

    /**
     * 收货地址地区
     */
    @NotNull(message = "收货地址地区不能为空", groups = ValidationGroups.Create.class)
    @Schema(description = "收货地址地区", requiredMode = Schema.RequiredMode.REQUIRED)
    private String district;

    /**
     * 收货地址详细信息
     */
    @NotNull(message = "收货地址详细信息不能为空", groups = ValidationGroups.Create.class)
    @Schema(description = "收货地址详细信息", requiredMode = Schema.RequiredMode.REQUIRED)
    private String detailAddress;

    /**
     * 是否默认
     */
    @Schema(description = "是否默认地址")
    private Boolean isDefault = false;  // 创建和更新均不必需，保持默认值

    /**
     * 地址标签(如"家"、"公司")
     */
    @Schema(description = "地址标签")
    private String tag;  // 创建和更新均不必需

}