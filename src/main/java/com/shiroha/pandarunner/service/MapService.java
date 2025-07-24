package com.shiroha.pandarunner.service;

import cn.hutool.json.JSON;

public interface MapService {

    /**
     * 获取三级地址的json数据
     *
     * @return json格式的三级地址
     */
    JSON getDistricts();

    /**
     * 获取地址的经纬度坐标
     *
     * @param address       地址
     * @return              经纬度
     */
    String getLocation(String address);

    /**
     * 获取两个经纬度点的距离
     *
     * @param origins     起始点坐标
     * @param destination 终点坐标
     * @return 两点的距离
     */
    JSON getDistance(String origins, String destination);

}
