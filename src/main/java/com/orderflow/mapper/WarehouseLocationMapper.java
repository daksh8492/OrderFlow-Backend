package com.orderflow.mapper;

import com.orderflow.dto.WarehouseLocationDto;
import com.orderflow.entity.warehouse.Warehouse;
import com.orderflow.entity.warehouse.WarehouseLocation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;
import java.util.UUID;

@Mapper(componentModel = "spring")
public interface WarehouseLocationMapper {

    @Mapping(source = "warehouseId", target = "warehouse", qualifiedByName = "uuidToWarehouse")
    @Mapping(source = "parentLocationId", target = "parentLocation", qualifiedByName = "uuidToParentLocation")
    @Mapping(target = "stocks", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    WarehouseLocation warehouseLocationDtoToWarehouseLocation(WarehouseLocationDto warehouseLocationDto);

    @Mapping(source = "warehouse.warehouseId", target = "warehouseId")
    @Mapping(source = "parentLocation.locationId", target = "parentLocationId")
    WarehouseLocationDto warehouseLocationToWarehouseLocationDto(WarehouseLocation warehouseLocation);

    List<WarehouseLocationDto> warehouseLocationsToWarehouseLocationDtos(List<WarehouseLocation> warehouseLocations);

    @Named("uuidToWarehouse")
    default Warehouse uuidToWarehouse(UUID warehouseId) {
        if (warehouseId == null) return null;
        Warehouse warehouse = new Warehouse();
        warehouse.setWarehouseId(warehouseId);
        return warehouse;
    }

    @Named("uuidToParentLocation")
    default WarehouseLocation uuidToParentLocation(UUID parentLocationId) {
        if (parentLocationId == null) return null;
        WarehouseLocation parent = new WarehouseLocation();
        parent.setLocationId(parentLocationId);
        return parent;
    }
}
