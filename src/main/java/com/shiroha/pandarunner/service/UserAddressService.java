package com.shiroha.pandarunner.service;

import com.mybatisflex.core.service.IService;
import com.shiroha.pandarunner.domain.dto.UserAddressDTO;
import com.shiroha.pandarunner.domain.entity.UserAddress;
import com.shiroha.pandarunner.domain.vo.UserAddressVO;

import java.util.List;

/**
 * 用户地址表 服务层。
 *
 * @author haowei703
 * @since 2025-07-03
 */
public interface UserAddressService extends IService<UserAddress> {

    /**
     * 根据地址ID获取地址
     *
     * @param addressId     地址ID
     * @return              地址
     */
    UserAddress getUserAddressById(Long addressId);

    /**
     * 获取用户的全部地址
     * @param userId        用户ID
     * @return              地址列表
     */
    List<UserAddressVO> getUserAddressesByUserId(Long userId);

    /**
     * 保存用户地址
     *
     * @param userId         用户ID
     * @param userAddressDTO dto
     * @return              地址ID
     */
    Long saveUserAddress(Long userId, UserAddressDTO userAddressDTO);

    /**
     * 更新用户地址
     *
     * @param userId         用户ID
     * @param addressId      地址ID
     * @param userAddressDTO dto
     */
    void updateUserAddress(Long userId, Long addressId, UserAddressDTO userAddressDTO);

    /**
     * 删除用户地址
     *
     * @param userId            用户ID
     * @param addressId         地址ID
     */
    void deleteUserAddress(Long userId, Long addressId);

    /**
     * 检验地址合法
     *
     * @param userId      用户ID
     * @param userAddress 地址
     */
    void validateAddressBelongToUser(Long userId, UserAddress userAddress);
}
