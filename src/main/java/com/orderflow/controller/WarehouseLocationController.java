package com.orderflow.controller;

import com.orderflow.dto.WarehouseLocationDto;
import com.orderflow.entity.warehouse.WarehouseLocation;
import com.orderflow.mapper.WarehouseLocationMapper;
import com.orderflow.service.WarehouseLocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/locations")
public class WarehouseLocationController {

    @Autowired
    private WarehouseLocationService warehouseLocationService;
    @Autowired
    private WarehouseLocationMapper warehouseLocationMapper;

    @PostMapping
    public ResponseEntity<WarehouseLocationDto> addWarehouseLocation(@RequestBody WarehouseLocationDto warehouseLocationDto){
        return new ResponseEntity<>(warehouseLocationMapper.warehouseLocationToWarehouseLocationDto(warehouseLocationService.addWarehouseLocation(warehouseLocationMapper.warehouseLocationDtoToWarehouseLocation(warehouseLocationDto))), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<WarehouseLocationDto>> getAllWarehouseLocations(){
        return new ResponseEntity<>(warehouseLocationMapper.warehouseLocationsToWarehouseLocationDtos(warehouseLocationService.getAllWarehouseLocations()), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<WarehouseLocationDto> getWarehouseLocationById(@PathVariable UUID id){
        return new ResponseEntity<>(warehouseLocationMapper.warehouseLocationToWarehouseLocationDto(warehouseLocationService.getWarehouseLocationById(id)), HttpStatus.OK);
    }

    @GetMapping("/warehouse/{id}")
    public ResponseEntity<List<WarehouseLocationDto>> getWarehouseLocationByWarehouseId(@PathVariable UUID id){
        return new ResponseEntity<>(warehouseLocationMapper.warehouseLocationsToWarehouseLocationDtos(warehouseLocationService.getAllLocationsFromWarehouse(id)), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteWarehouseLocationById(@PathVariable UUID id){
        warehouseLocationService.deleteWarehouseLocationById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/{id}/activate")
    public ResponseEntity<WarehouseLocationDto> activateWarehouseLocation(@PathVariable UUID id){
        return new ResponseEntity<>(warehouseLocationMapper.warehouseLocationToWarehouseLocationDto(warehouseLocationService.activateWarehouseLocationById(id)), HttpStatus.OK);
    }

    @PutMapping("/{id}/deactivate")
    public ResponseEntity<WarehouseLocationDto> deactivateWarehouseLocation(@PathVariable UUID id){
        return new ResponseEntity<>(warehouseLocationMapper.warehouseLocationToWarehouseLocationDto(warehouseLocationService.deactivateWarehouseLocationById(id)), HttpStatus.OK);
    }

    @GetMapping("/{id}/children")
    public ResponseEntity<List<WarehouseLocationDto>> getChildren(@PathVariable UUID id){
        return new ResponseEntity<>(warehouseLocationMapper.warehouseLocationsToWarehouseLocationDtos(warehouseLocationService.getChildrenLocations(id)), HttpStatus.OK);
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<List<WarehouseLocationDto>> getWarehouseLocationsByType(@PathVariable WarehouseLocation.WarehouseLocationType type){
        return new ResponseEntity<>(warehouseLocationMapper.warehouseLocationsToWarehouseLocationDtos(warehouseLocationService.getAllLocationFromType(type)), HttpStatus.OK);
    }

}
