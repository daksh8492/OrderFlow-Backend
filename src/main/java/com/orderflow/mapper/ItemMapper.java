package com.orderflow.mapper;

import com.orderflow.dto.ItemDto;
import com.orderflow.entity.product.Item;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {VariantMapper.class})
public interface ItemMapper {

    @Mapping(target = "variants", ignore = true)    // variants created separately via VariantService
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Item itemDtoToItem(ItemDto itemDto);

    ItemDto itemToItemDto(Item item);

    List<ItemDto> itemsToItemDtos(List<Item> items);
}
