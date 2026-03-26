package com.orderflow.mapper;

import com.orderflow.dto.ItemDto;
import com.orderflow.entity.product.Item;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ItemMapper {

    Item itemDtoToItem(ItemDto itemDto);

    ItemDto itemToItemDto(Item item);

    List<ItemDto> itemsToItemDtos(List<Item> items);
}
