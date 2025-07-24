package com.shiroha.pandarunner.util;

import com.shiroha.pandarunner.domain.entity.Address;

import java.util.Objects;

public class AddressUtils {

    /**
     * 比较两个地址是否具有相同的省、市和区信息
     * @param address1      地址1
     * @param address2      地址2
     * @return              {@code true}相同 {@code false}不同
     */
    public static boolean isSameDistrict(Address address1, Address address2) {
        if (address1 == address2) return true;
        if (address1 == null || address2 == null) return false;

        String province1 = address1.getProvince();
        String city1 = address1.getCity();
        String district1 = address1.getDistrict();

        String province2 = address2.getProvince();
        String city2 = address2.getCity();
        String district2 = address2.getDistrict();

        return Objects.equals(province1, province2) &&
                Objects.equals(city1, city2) &&
                Objects.equals(district1, district2);
    }

    /**
     * 拼接多个经纬度坐标为一个字符串，使用 | 分隔
     * @param coordinates 经纬度坐标数组，每个元素格式为 "经度,纬度"
     * @return 拼接后的字符串，格式为 "经度1,纬度1|经度2,纬度2|..."
     */
    public static String joinCoordinates(String... coordinates) {
        if (coordinates == null || coordinates.length == 0) {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < coordinates.length; i++) {
            if (i > 0) {
                sb.append("|");
            }
            sb.append(coordinates[i]);
        }
        return sb.toString();
    }
}
