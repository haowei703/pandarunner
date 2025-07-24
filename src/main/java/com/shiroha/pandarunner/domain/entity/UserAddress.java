package com.shiroha.pandarunner.domain.entity;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;

/**
 * 用户地址表 实体类。
 *
 * @author haowei703
 * @since 2025-07-03
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("t_user_address")
@EqualsAndHashCode(callSuper = true)
public class UserAddress extends BaseEntity implements Address, Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 地址ID
     */
    @Id(keyType = KeyType.Auto)
    private Long id;

    /**
     * 关联的用户ID
     */
    private Long userId;

    /**
     * 收货人姓名
     */
    private String receiverName;

    /**
     * 收货人电话
     */
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

    /**
     * 收货地址经纬度坐标
     */
    private String location;

    /**
     * 是否默认
     */
    private Boolean isDefault;

    /**
     * 地址标签(如"家"、"公司")
     */
    private String tag;

}
