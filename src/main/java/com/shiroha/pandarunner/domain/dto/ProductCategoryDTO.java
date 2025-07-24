package com.shiroha.pandarunner.domain.dto;

import com.shiroha.pandarunner.config.ValidationGroups;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * DTO for {@link com.shiroha.pandarunner.domain.entity.ProductCategory}
 */
@Data
public class ProductCategoryDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 关联的商家ID
     */
    @NotNull(message = "商家ID不能为空", groups = ValidationGroups.Create.class)
    private Long merchantId;

    /**
     * 商品分类名称
     */
    @NotBlank(message = "商品分类名称不能为空", groups = ValidationGroups.Create.class)
    @Size(max = 50, message = "分类名称长度不能超过50个字符",
            groups = {ValidationGroups.Create.class, ValidationGroups.Update.class})
    private String name;

    /**
     * 排序权重
     */
    private Integer sortOrder;

}