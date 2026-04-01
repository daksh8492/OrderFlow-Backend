package com.orderflow.mapper;

import com.orderflow.dto.WarehouseDto;
import com.orderflow.entity.warehouse.Warehouse;
import com.orderflow.entity.warehouse.WarehouseLocation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface WarehouseMapper {

    @Mapping(target = "locationIds", source = "locations", qualifiedByName = "locationsToIds")
    WarehouseDto warehouseToWarehouseDto(Warehouse warehouse);

    @Mapping(target = "locations", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Warehouse warehouseDtoToWarehouse(WarehouseDto warehouseDto);

    List<WarehouseDto> warehousesToWarehouseDtos(List<Warehouse> warehouses);

    @Named("locationsToIds")
    default Set<UUID> locationsToIds(Set<WarehouseLocation> locations) {
        if (locations == null) return new java.util.HashSet<>();
        return locations.stream()
                .map(WarehouseLocation::getLocationId)
                .collect(Collectors.toSet());
    }
}
