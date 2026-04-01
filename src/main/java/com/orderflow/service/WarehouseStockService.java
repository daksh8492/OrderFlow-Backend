package com.orderflow.service;

import com.orderflow.dto.WarehouseStockDto;
import com.orderflow.entity.product.Variant;
import com.orderflow.entity.warehouse.WarehouseLocation;
import com.orderflow.entity.warehouse.WarehouseStock;
import com.orderflow.mapper.WarehouseLocationMapper;
import com.orderflow.mapper.WarehouseStockMapper;
import com.orderflow.repository.warehouse.WarehouseStockRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WarehouseStockService {

    @Autowired
    private WarehouseStockRepo warehouseStockRepo;

    @Autowired
    private WarehouseLocationService warehouseLocationService;

    @Autowired
    private WarehouseStockMapper warehouseStockMapper;

    @Autowired
    private WarehouseLocationMapper warehouseLocationMapper;


    public WarehouseStockDto addWarehouseStock(WarehouseStockDto warehouseStockDto) {
        WarehouseStock warehouseStock = warehouseStockMapper.warehouseStockDtoToWarehouseStock(warehouseStockDto);
        WarehouseLocation location = warehouseLocationMapper.warehouseLocationDtoToWarehouseLocation(warehouseLocationService.getWarehouseLocationById(warehouseStock.getWarehouseLocation().getLocationId()));
        warehouseStock.setWarehouseLocation(location);
        warehouseStock.setWarehouse(location.getWarehouse());
        // Variant service bnake add krna h or uske baad check bhi lgana h ki same location pe same variant ka stock phlese to nhi h
        return warehouseStockMapper.warehouseStockToWarehouseStockDto(warehouseStockRepo.save(warehouseStock));
    }



}
