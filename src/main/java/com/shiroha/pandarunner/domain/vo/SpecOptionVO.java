package com.shiroha.pandarunner.domain.vo;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class SpecOptionVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id(keyType = KeyType.Auto)
    private Long id;

    /**
     * 规格选项名称
     */
    private String name;

    /**
     * 价格偏移量
     */
    private BigDecimal priceAdjust;

    /**
     * 是否默认选择
     */
    private Boolean isDefault;
}
