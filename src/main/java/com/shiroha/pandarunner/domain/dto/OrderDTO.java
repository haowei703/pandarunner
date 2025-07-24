package com.shiroha.pandarunner.domain.dto;

import com.shiroha.pandarunner.config.ValidationGroups;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * 用户提交订单DTO
 * <p>
 * DTO for {@link com.shiroha.pandarunner.domain.entity.Order}
 * </p>
 *
 * @author haowei703
 * @since 2025-7-3
 */
@Data
@Schema(description = "用户提交订单DTO")
public class OrderDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "收货地址ID", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "收货地址不能为空", groups = ValidationGroups.Create.class)
    private Long addressId;

    @Schema(description = "商家ID", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "商家ID不能为空", groups = ValidationGroups.Create.class)
    private Long merchantId;

    @Schema(description = "订单备注")
    @Size(max = 255, message = "订单备注长度不能超过255个字符",
            groups = {ValidationGroups.Create.class, ValidationGroups.Update.class})
    private String remark;

    @Schema(description = "餐具数量")
    @Min(value = -1, message = "餐具数量不能小于-1", groups = ValidationGroups.Create.class)
    @Max(value = 15, message = "餐具数量不能大于15", groups = ValidationGroups.Create.class)
    private Integer utensilsCount;

    @Valid
    @Schema(description = "商品项列表", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "商品项不能为空", groups = ValidationGroups.Create.class)
    private List<OrderItemDTO> items;
}