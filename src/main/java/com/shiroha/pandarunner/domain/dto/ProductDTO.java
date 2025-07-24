package com.shiroha.pandarunner.domain.dto;

import com.shiroha.pandarunner.config.ValidationGroups;
import com.shiroha.pandarunner.domain.entity.Product;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * DTO for {@link com.shiroha.pandarunner.domain.entity.Product}
 */
@Data
@Schema(description = "商品DTO")
public class ProductDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 关联的商家ID
     */
    @NotNull(message = "商家ID不能为空", groups = {ValidationGroups.Create.class, ValidationGroups.Update.class})
    private Long merchantId;

    /**
     * 分类ID
     */
    @NotNull(message = "分类ID不能为空", groups = ValidationGroups.Create.class)
    private Long categoryId;

    /**
     * 商品名称
     */
    @NotBlank(message = "商品名称不能为空", groups = ValidationGroups.Create.class)
    @Size(max = 100, message = "商品名称长度不能超过100个字符",
            groups = {ValidationGroups.Create.class, ValidationGroups.Update.class})
    private String name;

    /**
     * 商品描述
     */
    @Size(max = 500, message = "商品描述长度不能超过500个字符",
            groups = {ValidationGroups.Create.class, ValidationGroups.Update.class})
    private String description;

    /**
     * 是否默认展示
     */
    private Boolean isPriority = false;

    /**
     * 商品当前价格
     */
    @NotNull(message = "商品价格不能为空", groups = ValidationGroups.Create.class)
    @DecimalMin(value = "0.01", message = "商品价格不能小于0.01",
            groups = {ValidationGroups.Create.class, ValidationGroups.Update.class})
    private BigDecimal price;

    /**
     * 商品原始价格
     */
    @DecimalMin(value = "0.01", message = "商品原始价格不能小于0.01",
            groups = {ValidationGroups.Create.class, ValidationGroups.Update.class})
    private BigDecimal originalPrice;

    /**
     * 商品图片
     */
    @NotBlank(message = "商品图片不能为空", groups = ValidationGroups.Create.class)
    @Size(max = 255, message = "商品图片URL长度不能超过255个字符",
            groups = {ValidationGroups.Create.class, ValidationGroups.Update.class})
    private String image;

    /**
     * 库存(-1表示无限库存)
     */
    @Min(value = -1, message = "库存不能小于-1",
            groups = {ValidationGroups.Create.class, ValidationGroups.Update.class})
    private Integer stock;

    /**
     * 商品折扣
     */
    @Min(value = 0, message = "商品折扣不能为负数",
            groups = {ValidationGroups.Create.class, ValidationGroups.Update.class})
    @Max(value = 100, message = "商品折扣不能超过100%",
            groups = {ValidationGroups.Create.class, ValidationGroups.Update.class})
    private Integer sales;

    /**
     * 排序权重
     */
    private Integer sortOrder;

    /**
     * 状态(0-下架 1-上架)
     */
    private Product.ProductStatus status = Product.ProductStatus.ONLINE;

    /**
     * 规格信息
     */
    @Valid // 嵌套校验
    private List<SpecGroupDTO> specs;
}