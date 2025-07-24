package com.shiroha.pandarunner.domain.dto;

import com.shiroha.pandarunner.config.ValidationGroups;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * 单个商品项DTO
 *
 * @author haowei703
 * @since 2025-7-3
 */
@Data
@Schema(description = "下单商品项DTO")
public class OrderItemDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "商品ID", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "商品ID不能为空", groups = ValidationGroups.Create.class)
    private Long productId;

    @Schema(description = "购买数量", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "购买数量不能为空", groups = ValidationGroups.Create.class)
    @Min(value = 1, message = "购买数量不能小于1", groups = ValidationGroups.Create.class)
    private Integer quantity;

    @Schema(description = "规格组合ID列表")
    @NotEmpty(message = "规格组合不能为空", groups = ValidationGroups.Create.class)
    private List<Long> spec;
}