package com.shiroha.pandarunner.service.impl;

import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.shiroha.pandarunner.converter.UserAddressConverter;
import com.shiroha.pandarunner.domain.dto.UserAddressDTO;
import com.shiroha.pandarunner.domain.entity.UserAddress;
import com.shiroha.pandarunner.domain.vo.UserAddressVO;
import com.shiroha.pandarunner.exception.BusinessDataConsistencyException;
import com.shiroha.pandarunner.exception.BusinessDataNotFoundException;
import com.shiroha.pandarunner.mapper.UserAddressMapper;
import com.shiroha.pandarunner.service.MapService;
import com.shiroha.pandarunner.service.UserAddressService;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.shiroha.pandarunner.domain.entity.table.UserAddressTableDef.USER_ADDRESS;

/**
 * 用户地址表 服务层实现。
 *
 * @author haowei703
 * @since 2025-07-03
 */
@Service
@CacheConfig(cacheNames = "userAddress")
public class UserAddressServiceImpl extends ServiceImpl<UserAddressMapper, UserAddress> implements UserAddressService {

    private final UserAddressConverter converter;
    private final UserAddressService self;
    private final MapService mapService;

    public UserAddressServiceImpl(UserAddressConverter converter,
                                  @Lazy UserAddressService userAddressService, MapService mapService, MapService mapService1) {
        this.converter = converter;
        this.self = userAddressService;
        this.mapService = mapService1;
    }

    /**
     * 根据地址ID获取地址
     *
     * @param addressId     地址ID
     * @return              地址
     */
    @Override
    @Cacheable(key = "'addressId::' + #addressId")
    public UserAddress getUserAddressById(Long addressId) {
        UserAddress address = getById(addressId);
        if(address == null) {
            throw new BusinessDataNotFoundException("地址ID不存在", addressId);
        }
        return address;
    }

    /**
     * 获取用户的全部地址
     *
     * @param userId 用户ID
     * @return 地址列表
     */
    @Override
    @Cacheable(key = "'userId::' + #userId", unless = "#userId == null || #result == null || #result.isEmpty()")
    public List<UserAddressVO> getUserAddressesByUserId(Long userId) {
        List<UserAddress> userAddressList = list(query().where(USER_ADDRESS.USER_ID.eq(userId)));
        return converter.userAddressListToUserAddressVOList(userAddressList);
    }

    /**
     * 保存用户地址
     *
     * @param userId         用户ID
     * @param userAddressDTO dto
     * @return               地址ID
     */
    @Override
    @CacheEvict(key = "'userId::' + #userId")
    public Long saveUserAddress(Long userId, UserAddressDTO userAddressDTO) {
        updateAddressWithDefaultFlag(userId, userAddressDTO);

        UserAddress userAddress = converter.userAddressDtoToUserAddress(userAddressDTO);
        userAddress.setUserId(userId);
        userAddress.setLocation(mapService.getLocation(userAddressDTO.getAddress()));
        // 保存地址
        save(userAddress);
        return userAddress.getId();
    }

    /**
     * 更新用户地址
     *
     * @param userId        用户ID
     * @param addressId     地址ID
     * @param userAddressDTO dto
     */
    @Override
    @Caching(
            evict = {@CacheEvict(key = "'userId::' + #userId"), @CacheEvict(key = "'addressId::' + #addressId")}
    )
    public void updateUserAddress(Long userId, Long addressId, UserAddressDTO userAddressDTO) {
        UserAddress userAddress = self.getUserAddressById(addressId);
        // 检验地址合法
        validateAddressBelongToUser(userId, userAddress);

        updateAddressWithDefaultFlag(userId, userAddressDTO);

        UserAddress converted = converter.userAddressDtoToUserAddress(userAddressDTO);
        converted.setId(addressId);
        converted.setUserId(userId);
        // 当地址发生变动时重新获取地址坐标
        if(!userAddress.getAddress().equals(converted.getAddress())) {
            converted.setLocation(mapService.getLocation(converted.getAddress()));
        }
        // 更新地址
        updateById(converted);
    }

    /**
     * 在插入和更新时替换默认地址
     *
     * @param userId            用户ID
     * @param userAddressDTO    DTO对象
     */
    private void updateAddressWithDefaultFlag(Long userId, UserAddressDTO userAddressDTO) {
        List<UserAddress> userAddressList = list(query()
                .where(USER_ADDRESS.USER_ID.eq(userId))
                .and(USER_ADDRESS.IS_DEFAULT.eq(true)));

        // 如果已经存在默认地址并且更新地址为默认地址，则将已存在的默认地址设置为非默认
        if(!userAddressList.isEmpty() && userAddressDTO.getIsDefault()) {
            userAddressList.stream()
                    .filter(UserAddress::getIsDefault)
                    .forEach(userAddress -> {
                        userAddress.setIsDefault(false);
                        // 更新为非默认
                        updateById(userAddress);
                    });
        }
    }

    /**
     * 删除用户地址
     *
     * @param userId    用户ID
     * @param addressId 地址ID
     */
    @Override
    @Caching(
            evict = {@CacheEvict(key = "'userId::' + #userId"), @CacheEvict(key = "'addressId::' + #addressId")}
    )
    public void deleteUserAddress(Long userId, Long addressId) {
        UserAddress userAddress = self.getUserAddressById(addressId);

        // 检验地址合法
        validateAddressBelongToUser(userId, userAddress);

        removeById(addressId);
    }

    /**
     * 检验地址合法
     *
     * @param userId      用户ID
     * @param userAddress 地址
     */
    @Override
    public void validateAddressBelongToUser(Long userId, UserAddress userAddress) {
        // 检验地址属于用户
        if(!userAddress.getUserId().equals(userId)) {
            throw new BusinessDataConsistencyException(
                    String.format("地址[%s]不属于用户[%s]", userAddress.getId(), userId),
                    userAddress.getId(),
                    userId,
                    BusinessDataConsistencyException.ErrorType.RELATION_MISMATCH);
        }
    }
}
