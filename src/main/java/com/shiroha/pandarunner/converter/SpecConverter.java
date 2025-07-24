package com.shiroha.pandarunner.converter;

import com.shiroha.pandarunner.domain.dto.SpecGroupDTO;
import com.shiroha.pandarunner.domain.dto.SpecOptionDTO;
import com.shiroha.pandarunner.domain.entity.SpecGroup;
import com.shiroha.pandarunner.domain.entity.SpecOption;
import com.shiroha.pandarunner.domain.vo.SpecGroupVO;
import com.shiroha.pandarunner.domain.vo.SpecOptionVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SpecConverter {

    @Mapping(target = "options", ignore = true)
    SpecGroupVO specGroupToSpecGroupVO(SpecGroup specGroup);

    List<SpecGroupVO> specGroupListToSpecGroupVOList(List<SpecGroup> specGroups);

    SpecGroup specGroupDTOToSpecGroup(SpecGroupDTO specGroupDTO);

    List<SpecGroup> specGroupDtoListToSpecGroupList(List<SpecGroupDTO> specGroupDTOs);

    SpecOptionVO specOptionToSpecOptionVO(SpecOption specOption);

    List<SpecOption> specOptionDTOListToSpecOptionList(List<SpecOptionDTO> options);

    List<SpecOptionVO> specOptionListToSpecOptionVOList(List<SpecOption> options);
}
