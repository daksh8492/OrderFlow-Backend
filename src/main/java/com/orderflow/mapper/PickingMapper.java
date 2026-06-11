package com.orderflow.mapper;


import com.orderflow.dto.PickingDto;
import com.orderflow.dto.PickingSummaryDto;
import com.orderflow.entity.picking.Picking;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.Set;

@Mapper(
        componentModel = "spring",
        uses = {PickingItemMapper.class}
)
public interface PickingMapper {

    @Mapping(target = "pickerId", source = "picker.userId")
    @Mapping(target = "orderId", source = "order.orderId")
    @Mapping(target = "warehouseId", source = "warehouse.warehouseId")
    PickingDto pickingToPickingDto(Picking picking);

    @Mapping(target = "picker", ignore = true)
    @Mapping(target = "order", ignore = true)
    @Mapping(target = "warehouse", ignore = true)
    Picking pickingDtoToPicking(PickingDto dto);

    List<PickingDto> pickingsToPickingDtos(List<Picking> pickings);

    Set<PickingDto> pickingsToPickingDtos(Set<Picking> pickings);

    @Mapping(target = "pickerId", source = "picker.userId")
    @Mapping(target = "orderId", source = "order.orderId")
    @Mapping(target = "warehouseId", source = "warehouse.warehouseId")
    PickingSummaryDto pickingToPickingSummaryDto(Picking picking);

    List<PickingSummaryDto> pickingsToPickingSummaryDtos(List<Picking> pickings);
}