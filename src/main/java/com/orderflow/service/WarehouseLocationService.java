package com.orderflow.service;

import com.orderflow.dto.WarehouseLocationDto;
import com.orderflow.entity.warehouse.Warehouse;
import com.orderflow.entity.warehouse.WarehouseLocation;
import com.orderflow.exceptions.WarehouseLocationNotFoundException;
import com.orderflow.exceptions.WarehouseLocationTypeInvalidException;
import com.orderflow.exceptions.WarehouseNotFoundException;
import com.orderflow.mapper.WarehouseLocationMapper;
import com.orderflow.mapper.WarehouseMapper;
import com.orderflow.repository.warehouse.WarehouseLocationRepo;
import com.orderflow.repository.warehouse.WarehouseRepo;
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
    @Autowired
    private WarehouseLocationMapper warehouseLocationMapper;
    @Autowired
    private WarehouseRepo warehouseRepo;
    @Autowired
    private WarehouseMapper warehouseMapper;

    public WarehouseLocationDto addWarehouseLocation(WarehouseLocationDto warehouseLocationDto) {
        WarehouseLocation warehouseLocation = warehouseLocationMapper.warehouseLocationDtoToWarehouseLocation(warehouseLocationDto);
        Warehouse warehouse = warehouseRepo.findById(warehouseLocation.getWarehouse().getWarehouseId()).orElseThrow( () -> new WarehouseNotFoundException("Warehouse does not exist"));
        warehouseLocation.setWarehouse(warehouse);

        WarehouseLocation parent = null;

        if (warehouseLocation.getParentLocation() != null){
            parent = warehouseLocationRepo.findById(warehouseLocation.getParentLocation().getLocationId()).orElseThrow( () -> new WarehouseLocationNotFoundException("Parent Warehouse location not found"));
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
        return warehouseLocationMapper.warehouseLocationToWarehouseLocationDto(warehouseLocationRepo.save(warehouseLocation));
    }

    public List<WarehouseLocationDto> getAllWarehouseLocations() {
        return warehouseLocationMapper.warehouseLocationsToWarehouseLocationDtos(warehouseLocationRepo.findAll());
    }

    public WarehouseLocationDto getWarehouseLocationById(UUID id) {
        return warehouseLocationMapper.warehouseLocationToWarehouseLocationDto(warehouseLocationRepo.findById(id).orElseThrow(() -> new WarehouseLocationNotFoundException("Warehouse location not found")));
    }

    public List<WarehouseLocationDto> getAllLocationsFromWarehouse(UUID id) {
        return warehouseLocationMapper.warehouseLocationsToWarehouseLocationDtos(warehouseLocationRepo.findByWarehouse_WarehouseId(id));
    }

    public void deleteWarehouseLocationById(UUID id) {
        WarehouseLocation warehouseLocation = warehouseLocationMapper.warehouseLocationDtoToWarehouseLocation(getWarehouseLocationById(id));
        warehouseLocationRepo.delete(warehouseLocation);
    }

    public WarehouseLocationDto activateWarehouseLocationById(UUID id) {
        WarehouseLocation warehouseLocation = warehouseLocationMapper.warehouseLocationDtoToWarehouseLocation(getWarehouseLocationById(id));
        warehouseLocation.setActive(true);
        return warehouseLocationMapper.warehouseLocationToWarehouseLocationDto(warehouseLocationRepo.save(warehouseLocation));
    }

    public WarehouseLocationDto deactivateWarehouseLocationById(UUID id) {
        WarehouseLocation warehouseLocation = warehouseLocationMapper.warehouseLocationDtoToWarehouseLocation(getWarehouseLocationById(id));
        warehouseLocation.setActive(false);
        return warehouseLocationMapper.warehouseLocationToWarehouseLocationDto(warehouseLocationRepo.save(warehouseLocation));
    }

    public List<WarehouseLocationDto> getChildrenLocations(UUID id){
        return warehouseLocationMapper.warehouseLocationsToWarehouseLocationDtos(warehouseLocationRepo.findAllByParentLocation_LocationId(id));
    }

    public List<WarehouseLocationDto> getAllLocationFromType(WarehouseLocation.WarehouseLocationType type){
        return warehouseLocationMapper.warehouseLocationsToWarehouseLocationDtos(warehouseLocationRepo.findAllByLocationType(type));
    }
}


