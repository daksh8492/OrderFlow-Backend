package com.orderflow.mapper;

import com.orderflow.dto.WarehouseStockDto;
import com.orderflow.entity.warehouse.WarehouseStock;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface WarehouseStockMapper {

    WarehouseStockDto warehouseStockToWarehouseStockDto(WarehouseStock warehouseStock);

    WarehouseStock warehouseStockDtoToWarehouseStock(WarehouseStockDto warehouseStockDto);

    List<WarehouseStockDto> warehouseStocksToWarehouseStockDtos(List<WarehouseStock> warehouseStocks);
}
