package com.orderflow.mapper;

import com.orderflow.dto.ItemDto;
import com.orderflow.dto.ItemSummaryDto;
import com.orderflow.entity.product.Item;
import com.orderflow.entity.product.Variant;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {VariantMapper.class})
public interface ItemMapper {

    @Mapping(target = "variants", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Item itemDtoToItem(ItemDto itemDto);

    ItemDto itemToItemDto(Item item);

    List<ItemDto> itemsToItemDtos(List<Item> items);

    @Mapping(target = "variantCount", expression = "java(item.getVariants() == null ? 0 : item.getVariants().size())")
    @Mapping(target = "imageUrl", expression = "java(firstImageUrl(item))")
    ItemSummaryDto itemToItemSummaryDto(Item item);

    List<ItemSummaryDto> itemsToItemSummaryDtos(List<Item> items);

    default String firstImageUrl(Item item) {
        if (item.getVariants() == null) return null;
        return item.getVariants().stream()
                .map(Variant::getImageUrls)
                .filter(urls -> urls != null && !urls.isEmpty())
                .map(urls -> urls.get(0))
                .findFirst()
                .orElse(null);
    }
}