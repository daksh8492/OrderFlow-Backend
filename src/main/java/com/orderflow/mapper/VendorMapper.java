package com.orderflow.mapper;

import com.orderflow.dto.VendorDto;
import com.orderflow.entity.vendor.Vendor;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface VendorMapper {

    VendorDto vendorToVendorDto(Vendor vendor);

    Vendor vendorDtoToVendor(VendorDto vendorDto);

    List<VendorDto> vendorsToVendorDtos(List<Vendor> vendors);
}
