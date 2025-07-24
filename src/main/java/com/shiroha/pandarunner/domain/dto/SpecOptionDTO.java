package com.shiroha.pandarunner.domain.dto;

import com.shiroha.pandarunner.config.ValidationGroups;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * DTO for {@link com.shiroha.pandarunner.domain.entity.SpecOption}
 */
@Data
public class SpecOptionDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 规格选项名称
     */
    @NotBlank(message = "规格选项名称不能为空", groups = ValidationGroups.Create.class)
    @Size(max = 50, message = "规格选项名称长度不能超过50个字符",
            groups = {ValidationGroups.Create.class, ValidationGroups.Update.class})
    private String name;

    /**
     * 价格偏移量
     */
    @NotNull(message = "价格偏移量不能为空", groups = ValidationGroups.Create.class)
    @DecimalMin(value = "0.00", message = "价格偏移量不能为负数",
            groups = {ValidationGroups.Create.class, ValidationGroups.Update.class})
    private BigDecimal priceAdjust;

    /**
     * 是否默认选择
     */
    // 创建和更新时均不强制要求，由业务逻辑决定默认值
    private Boolean isDefault;
}