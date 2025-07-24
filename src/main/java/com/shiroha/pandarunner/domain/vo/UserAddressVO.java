package com.shiroha.pandarunner.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
@Schema(description = "用户VO")
public class UserAddressVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "地址ID")
    private Long id;

    /**
     * 收货人姓名
     */
    @NotNull(message = "收货人姓名不能为空")
    @Size(max= 20, message="编码长度不能超过20")
    @Schema(description = "收货人姓名")
    private String receiverName;

    /**
     * 收货人电话
     */
    @NotBlank(message="电话不能为空")
    @Size(min = 11, max = 11, message="电话长度必须是11位")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "电话格式错误")
    @Schema(description = "收货人电话")
    private String receiverPhone;

    /**
     * 收货地址省份
     */
    private String province;

    /**
     * 收货地址城市
     */
    private String city;

    /**
     * 收货地址地区
     */
    private String district;

    /**
     * 收货地址详细信息
     */
    private String detailAddress;

    @NotNull(message = "收货地址不能为空")
    @Schema(description = "收货地址")
    private String address;

    /**
     * 是否默认
     */
    @Schema(description = "是否默认地址")
    private Boolean isDefault = false;

    /**
     * 地址标签(如\"家\"、\"公司\")
     */
    @Schema(description = "地址标签")
    private String tag;

}
