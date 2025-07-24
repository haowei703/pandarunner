package com.shiroha.pandarunner.domain.dto;

import com.shiroha.pandarunner.config.ValidationGroups;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

/**
 * DTO for {@link com.shiroha.pandarunner.domain.entity.DeliveryRider}
 *
 * @author haowei703
 * @since 2025-07-03
 */
@Data
public class DeliveryRiderDTO {

    /**
     * 骑手姓名
     */
    @NotBlank(message = "骑手姓名不能为空", groups = {ValidationGroups.Create.class})
    private String name;

    /**
     * 骑手电话
     */
    @NotBlank(message = "骑手电话不能为空", groups = {ValidationGroups.Create.class})
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确", groups = {ValidationGroups.Create.class})
    private String phone;

    /**
     * 骑手身份证号
     */
    @NotBlank(message = "身份证号不能为空", groups = {ValidationGroups.Create.class})
    @Pattern(regexp = "^[1-9]\\d{5}(18|19|20)\\d{2}((0[1-9])|(1[0-2]))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$", 
             message = "身份证号格式不正确", groups = {ValidationGroups.Create.class})
    private String idCard;

    /**
     * 骑手位置
     */
    private String currentLocation;
}