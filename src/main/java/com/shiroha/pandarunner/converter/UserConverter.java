package com.shiroha.pandarunner.converter;

import com.shiroha.pandarunner.domain.dto.UserDTO;
import com.shiroha.pandarunner.domain.entity.User;
import com.shiroha.pandarunner.domain.vo.UserVO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserConverter {

    User userDtoToUser(UserDTO userDTO);

    UserVO userToUserVO(User user);

}
