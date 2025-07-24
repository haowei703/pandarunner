package com.shiroha.pandarunner.domain.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Data
public class SpecGroupVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 标识ID
     */
    private Long id;

    /**
     * 规格名称
     */
    private String name;

    /**
     * 是否必选
     */
    private Boolean isRequired;

    /**
     * 规格选项
     */
    private List<SpecOptionVO> options;
}
