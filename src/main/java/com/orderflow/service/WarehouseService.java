package com.orderflow.service;

import com.orderflow.entity.warehouse.Warehouse;
import com.orderflow.exceptions.WarehouseNotFoundException;
import com.orderflow.repository.warehouse.WarehouseRepo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class WarehouseService {

    @Autowired
    private WarehouseRepo warehouseRepo;

    public Warehouse addWarehouse(Warehouse warehouse) {
        Long count = warehouseRepo.count();
        String code;
        do {
            code = "WH-" + String.format("%04d", count + 1);
            count++;
        } while (warehouseRepo.existsByCode(code));
        warehouse.setCode(code);
        return warehouseRepo.save(warehouse);
    }

    public List<Warehouse> getAllWarehouses() {
        return warehouseRepo.findAll();
    }

    public Warehouse getWarehouseById(UUID id) {
        return warehouseRepo.findById(id).orElseThrow(() -> new WarehouseNotFoundException("Warehouse not found"));
    }

    public Warehouse getWarehouseByCode(String code){
        return warehouseRepo.findByCode(code).orElseThrow( () -> new WarehouseNotFoundException("Warehouse not found"));
    }

    public Warehouse updateWarehouse(UUID id, Warehouse warehouse){
        Warehouse existingWarehouse = getWarehouseById(id);
        BeanUtils.copyProperties(warehouse, existingWarehouse, "warehouseId", "code");
        return warehouseRepo.save(existingWarehouse);
    }

    public void deleteWarehouse(UUID id){
        Warehouse warehouse = getWarehouseById(id);
        if (!warehouse.getLocations().isEmpty()) {
            throw new IllegalStateException("Warehouse with locations cannot be deleted");
        }
        warehouseRepo.delete(warehouse);
    }
}
