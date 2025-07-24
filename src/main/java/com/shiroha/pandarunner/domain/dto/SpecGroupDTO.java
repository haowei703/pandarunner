package com.shiroha.pandarunner.domain.dto;

import com.shiroha.pandarunner.config.ValidationGroups;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * DTO for {@link com.shiroha.pandarunner.domain.entity.SpecGroup}
 */
@Data
public class SpecGroupDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 规格名称
     */
    @NotBlank(message = "规格名称不能为空", groups = ValidationGroups.Create.class)
    @Size(max = 50, message = "规格名称长度不能超过50个字符",
            groups = {ValidationGroups.Create.class, ValidationGroups.Update.class})
    private String name;

    /**
     * 是否必选
     */
    @NotNull(message = "是否必选不能为空", groups = ValidationGroups.Create.class)
    private Boolean isRequired;

    /**
     * 规格选项
     */
    @NotEmpty(message = "规格选项不能为空", groups = ValidationGroups.Create.class)
    @Valid  // 嵌套校验选项
    private List<SpecOptionDTO> options;
}