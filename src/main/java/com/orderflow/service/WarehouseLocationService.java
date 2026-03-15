package com.orderflow.service;

import com.orderflow.entity.warehouse.Warehouse;
import com.orderflow.entity.warehouse.WarehouseLocation;
import com.orderflow.exceptions.WarehouseLocationNotFoundException;
import com.orderflow.exceptions.WarehouseLocationTypeInvalidException;
import com.orderflow.repository.warehouse.WarehouseLocationRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class WarehouseLocationService {

    @Autowired
    private WarehouseLocationRepo warehouseLocationRepo;
    @Autowired
    private WarehouseService warehouseService;

    public WarehouseLocation addWarehouseLocation(WarehouseLocation warehouseLocation) {
        Warehouse warehouse = warehouseService.getWarehouseById(warehouseLocation.getWarehouse().getWarehouseId());
        warehouseLocation.setWarehouse(warehouse);

        WarehouseLocation parent = null;

        if (warehouseLocation.getParentLocation() != null){

        parent = getWarehouseLocationById(warehouseLocation.getParentLocation().getLocationId());
        }
        warehouseLocation.setParentLocation(parent);

        Long count = warehouseLocationRepo.countByWarehouseAndLocationTypeAndParentLocation(warehouseLocation.getWarehouse(), warehouseLocation.getLocationType(), warehouseLocation.getParentLocation());
        String code;
        switch (warehouseLocation.getLocationType()) {
            case ZONE -> {
                if (warehouseLocation.getParentLocation() != null)
                    throw new WarehouseLocationTypeInvalidException("Invalid parent location for location type ZONE");
                char next = (char) ('A' + count);
                code = String.valueOf(next);
            }
            case ROW -> {
                if (warehouseLocation.getParentLocation().getLocationType() != WarehouseLocation.WarehouseLocationType.ZONE) throw new WarehouseLocationTypeInvalidException("Invalid parent location for location type ROW");
                code = String.valueOf(count + 1);
            }
            case RACK -> {
                if (warehouseLocation.getParentLocation().getLocationType() != WarehouseLocation.WarehouseLocationType.ROW) throw new WarehouseLocationTypeInvalidException("Invalid location type RACK");
                char next = (char) ('a' + count);
                code = String.valueOf(next);
            }
            case BIN -> {
                if (warehouseLocation.getParentLocation().getLocationType() != WarehouseLocation.WarehouseLocationType.RACK) throw new WarehouseLocationTypeInvalidException("Invalid location type BIN");
                code = String.valueOf(count + 1);
            }
            default -> throw new IllegalArgumentException("invalid location type");
        }
        warehouseLocation.setCode(code);
        if (warehouseLocation.getParentLocation() != null) {
            warehouseLocation.setLocationName(warehouseLocation.getParentLocation().getLocationName() + "-" + code);
        } else {
            warehouseLocation.setLocationName(code);
        }
        warehouseLocation.setActive(true);
        return warehouseLocationRepo.save(warehouseLocation);
    }

    public List<WarehouseLocation> getAllWarehouseLocations() {
        return warehouseLocationRepo.findAll();
    }

    public WarehouseLocation getWarehouseLocationById(UUID id) {
        return warehouseLocationRepo.findById(id).orElseThrow(() -> new WarehouseLocationNotFoundException("Warehouse location not found"));
    }

    public List<WarehouseLocation> getAllLocationsFromWarehouse(UUID id) {
        return warehouseLocationRepo.findByWarehouse_WarehouseId(id);
    }

    public void deleteWarehouseLocationById(UUID id) {
        WarehouseLocation warehouseLocation = getWarehouseLocationById(id);
        warehouseLocationRepo.delete(warehouseLocation);
    }

    public WarehouseLocation activateWarehouseLocationById(UUID id) {
        WarehouseLocation warehouseLocation = getWarehouseLocationById(id);
        warehouseLocation.setActive(true);
        return warehouseLocationRepo.save(warehouseLocation);
    }

    public WarehouseLocation deactivateWarehouseLocationById(UUID id) {
        WarehouseLocation warehouseLocation = getWarehouseLocationById(id);
        warehouseLocation.setActive(false);
        return warehouseLocationRepo.save(warehouseLocation);
    }

    public List<WarehouseLocation> getChildrenLocations(UUID id){
        return warehouseLocationRepo.findAllByParentLocation_LocationId(id);
    }

    public List<WarehouseLocation> getAllLocationFromType(WarehouseLocation.WarehouseLocationType type){
        return warehouseLocationRepo.findAllByLocationType(type);
    }
}


