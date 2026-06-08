package com.orderflow.mapper;


import com.orderflow.dto.PickingItemDto;
import com.orderflow.entity.picking.PickingItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Set;

@Mapper(componentModel = "spring")
public interface PickingItemMapper {

    @Mapping(target = "pickingId", source = "picking.pickingId")
    @Mapping(target = "orderItemId", source = "orderItem.orderItemId")
    @Mapping(target = "warehouseStockId", source = "pickedFrom.stockId")
    PickingItemDto pickingItemToPickingItemDto(PickingItem pickingItem);

    @Mapping(target = "picking", ignore = true)
    @Mapping(target = "orderItem", ignore = true)
    @Mapping(target = "pickedFrom", ignore = true)
    PickingItem pickingItemDtoToPickingItem(PickingItemDto dto);

    Set<PickingItemDto> pickingItemsToPickingItemDtos(Set<PickingItem> pickingItems);

    Set<PickingItem> pickingItemDtosToPickingItems(Set<PickingItemDto> pickingItemDtos);
}
