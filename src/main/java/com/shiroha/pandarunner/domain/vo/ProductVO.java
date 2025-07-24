package com.shiroha.pandarunner.domain.vo;

import com.shiroha.pandarunner.domain.entity.Product;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class ProductVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 商品ID
     */
    private Long id;

    /**
     * 商品名称
     */
    private String name;

    /**
     * 商品描述
     */
    private String description;

    /**
     * 商品当前价格
     */
    private BigDecimal price;

    /**
     * 商品原始价格
     */
    private BigDecimal originalPrice;

    /**
     * 商品图片
     */
    private String image;

    /**
     * 商品折扣
     */
    private Integer sales;

    /**
     * 排序权重
     */
    private Integer sortOrder;

    /**
     * 状态(0-下架 1-上架)
     */
    private Product.ProductStatus status = Product.ProductStatus.ONLINE;
}
