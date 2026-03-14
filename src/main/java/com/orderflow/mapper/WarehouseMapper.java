package com.orderflow.mapper;

import com.orderflow.dto.WarehouseDto;
import com.orderflow.entity.warehouse.Warehouse;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface WarehouseMapper {

    WarehouseDto warehouseToWarehouseDto(Warehouse warehouse);

    Warehouse warehouseDtoToWarehouse(WarehouseDto warehouseDto);

    List<WarehouseDto> warehousesToWarehouseDtos(List<Warehouse> warehouses);
}
