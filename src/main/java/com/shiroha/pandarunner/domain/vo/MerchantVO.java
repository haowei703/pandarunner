package com.shiroha.pandarunner.domain.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;


@Data
public class MerchantVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 商家ID
     */
    private Long id;

    /**
     * 商家名称
     */
    private String name;

    /**
     * 商家描述
     */
    private String description;

    /**
     * 商家Logo图片URL
     */
    private String logo;

    /**
     * 距离
     */
    private double distance;

    /**
     * 平均配送时间(分钟)
     */
    private Integer avgDeliveryTime;

    /**
     * 最低起送金额
     */
    private BigDecimal minOrderAmount;

    /**
     * 配送费
     */
    private BigDecimal deliveryFee;

    /**
     * 打包费用
     */
    private BigDecimal packingFee;

    /**
     * 商家评分(1.0-5.0)
     */
    private BigDecimal score;

    /**
     * 月销量
     */
    private Integer monthlySales;

    /**
     * 历史总销量
     */
    private Integer totalSales;

    /**
     * 优先展示的商品列表
     */
    private List<ProductVO> products;

}
