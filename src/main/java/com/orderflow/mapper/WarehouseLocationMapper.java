package com.orderflow.mapper;

import com.orderflow.dto.WarehouseLocationDto;
import com.orderflow.entity.warehouse.WarehouseLocation;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface WarehouseLocationMapper {

    WarehouseLocation warehouseLocationDtoToWarehouseLocation(WarehouseLocationDto warehouseLocationDto);

    WarehouseLocationDto warehouseLocationToWarehouseLocationDto(WarehouseLocation warehouseLocation);

    List<WarehouseLocationDto> warehouseLocationsToWarehouseLocationDtos(List<WarehouseLocation> warehouseLocations);
}
