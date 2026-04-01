package com.orderflow.mapper;

import com.orderflow.dto.VendorDto;
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
public interface VendorMapper {

    @Mapping(source = "variants", target = "variantIds", qualifiedByName = "variantsToIds")
    VendorDto vendorToVendorDto(Vendor vendor);

    @Mapping(target = "variants", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Vendor vendorDtoToVendor(VendorDto vendorDto);

    List<VendorDto> vendorsToVendorDtos(List<Vendor> vendors);

    @Named("variantsToIds")
    default Set<UUID> variantsToIds(Set<Variant> variants) {
        if (variants == null) {return new HashSet<>();}
        return variants.stream().map(Variant::getVariantId).collect(Collectors.toSet());

    }
}
