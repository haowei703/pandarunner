package com.shiroha.pandarunner.converter;

import com.shiroha.pandarunner.domain.dto.MerchantDTO;
import com.shiroha.pandarunner.domain.entity.Merchant;
import com.shiroha.pandarunner.domain.vo.MerchantVO;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MerchantConverter {

    Merchant merchantDtoToMerchant(MerchantDTO merchantDTO);

    MerchantVO merchantToMerchantVO(Merchant merchant);

    List<MerchantVO> merchantListToMerchantVOList(List<Merchant> merchants);

}
