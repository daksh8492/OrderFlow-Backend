package com.orderflow.service;

import com.orderflow.dto.WarehouseDto;
import com.orderflow.entity.warehouse.Warehouse;
import com.orderflow.exceptions.WarehouseNotFoundException;
import com.orderflow.mapper.WarehouseMapper;
import com.orderflow.repository.warehouse.WarehouseRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class WarehouseService {

    @Autowired
    private WarehouseRepo warehouseRepo;
    @Autowired
    private WarehouseMapper warehouseMapper;
    Logger logger = LoggerFactory.getLogger(WarehouseService.class);

    public WarehouseDto addWarehouse(WarehouseDto warehouseDto) {
        Warehouse warehouse = warehouseMapper.warehouseDtoToWarehouse(warehouseDto);
        Long count = warehouseRepo.count();
        String code;
        do {
            code = "WH-" + String.format("%04d", count + 1);
            count++;
        } while (warehouseRepo.existsByCode(code));
        warehouse.setCode(code);
        return warehouseMapper.warehouseToWarehouseDto(warehouseRepo.save(warehouse));
    }

    public List<WarehouseDto> getAllWarehouses() {
        return warehouseMapper.warehousesToWarehouseDtos(warehouseRepo.findAll());
    }

    public WarehouseDto getWarehouseById(UUID id) {
        return warehouseMapper.warehouseToWarehouseDto(warehouseRepo.findById(id).orElseThrow(() -> new WarehouseNotFoundException("Warehouse not found")));
    }

    public WarehouseDto getWarehouseByCode(String code) {
        return warehouseMapper.warehouseToWarehouseDto(warehouseRepo.findByCode(code).orElseThrow(() -> new WarehouseNotFoundException("Warehouse not found")));
    }

    public WarehouseDto updateWarehouse(UUID id, WarehouseDto warehouseDto) {
        Warehouse warehouse = warehouseMapper.warehouseDtoToWarehouse(warehouseDto);
        Warehouse existingWarehouse = warehouseRepo.findById(id).orElseThrow( () -> new WarehouseNotFoundException("Warehouse not found"));
        if (warehouse.getName() != null) existingWarehouse.setName(warehouse.getName());
        if (warehouse.getAddress() != null) existingWarehouse.setAddress(warehouse.getAddress());
        if (warehouse.getCity() != null) existingWarehouse.setCity(warehouse.getCity());
        if (warehouse.getStatus() != null) existingWarehouse.setStatus(warehouse.getStatus());
        return warehouseMapper.warehouseToWarehouseDto(warehouseRepo.save(existingWarehouse));
    }

    public void deleteWarehouse(UUID id) {
        Warehouse warehouse = warehouseRepo.findById(id).orElseThrow( () -> new WarehouseNotFoundException("Warehouse does not exist"));
        if (warehouse.getLocations() != null && !warehouse.getLocations().isEmpty()) {
            logger.info(warehouse.getLocations().toString());
            throw new IllegalStateException("Warehouse with locations cannot be deleted");
        }
        warehouseRepo.delete(warehouse);
    }
}
