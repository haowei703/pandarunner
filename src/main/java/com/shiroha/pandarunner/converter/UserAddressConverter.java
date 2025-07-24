package com.shiroha.pandarunner.converter;

import com.shiroha.pandarunner.domain.dto.UserAddressDTO;
import com.shiroha.pandarunner.domain.entity.UserAddress;
import com.shiroha.pandarunner.domain.vo.UserAddressVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserAddressConverter {

    UserAddressDTO userAddressToUserAddressDTO(UserAddress userAddress);

    UserAddress userAddressDtoToUserAddress(UserAddressDTO userAddressDTO);

    @Mapping(target = "address", expression = "java(userAddress.getAddress())")
    UserAddressVO userAddressToUserAddressVO(UserAddress userAddress);

    List<UserAddressVO> userAddressListToUserAddressVOList(List<UserAddress> userAddresses);
}
