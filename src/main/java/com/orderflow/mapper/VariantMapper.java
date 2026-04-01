package com.orderflow.mapper;

import com.orderflow.dto.VariantDto;
import com.orderflow.entity.product.Item;
import com.orderflow.entity.product.Variant;
import com.orderflow.entity.vendor.Vendor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface VariantMapper {

    @Mapping(source = "itemId", target = "item", qualifiedByName = "uuidToItem")
    @Mapping(target = "vendors", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Variant variantDtoToVariant(VariantDto variantDto);

    @Mapping(source = "item.itemId", target = "itemId")
    @Mapping(target = "vendorIds", source = "vendors", qualifiedByName = "vendorsToIds")
    @Mapping(target = "qtyAvailForSales", ignore = true)
    VariantDto variantToVariantDto(Variant variant);

    List<VariantDto> variantsToVariantDtos(List<Variant> variants);

    @Named("uuidToItem")
    default Item uuidToItem(UUID itemId) {
        if (itemId == null) return null;
        Item item = new Item();
        item.setItemId(itemId);
        return item;
    }

    @Named("vendorsToIds")
    default Set<UUID> vendorsToIds(Set<Vendor> vendors) {
        if (vendors == null) return new HashSet<>();
        return vendors.stream()
                .map(Vendor::getVendorId)
                .collect(Collectors.toSet());
    }
}
