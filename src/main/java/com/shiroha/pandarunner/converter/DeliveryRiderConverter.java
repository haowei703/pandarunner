package com.shiroha.pandarunner.converter;

import com.shiroha.pandarunner.domain.dto.DeliveryRiderDTO;
import com.shiroha.pandarunner.domain.entity.DeliveryRider;
import com.shiroha.pandarunner.domain.vo.DeliveryRiderVO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DeliveryRiderConverter {

    DeliveryRider deliveryDtoToDelivery(DeliveryRiderDTO deliveryRiderDTO);

    DeliveryRiderVO deliveryRiderToDeliveryRiderVO(DeliveryRider deliveryRider);
}
