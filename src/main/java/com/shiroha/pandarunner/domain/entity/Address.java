package com.shiroha.pandarunner.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 地址显式接口
 */
public interface Address {

    /**
     * 获取省份
     */
    String getProvince();

    /**
     * 获取城市
     */
    String getCity();

    /**
     * 获取地区
     */
    String getDistrict();

    /**
     * 获取详细地址
     */
    String getDetailAddress();

    /**
     * 默认方法，返回完整地址
     *
     * @return      省、市、区、详细地址
     */
    @JsonIgnore
    default String getAddress() {
        return Stream.of(getProvince(), getCity(), getDistrict(), getDetailAddress())
                .filter(Objects::nonNull)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.joining());
    }

}
