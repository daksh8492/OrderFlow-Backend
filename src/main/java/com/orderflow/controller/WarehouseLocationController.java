package com.orderflow.controller;

import com.orderflow.dto.WarehouseLocationDto;
import com.orderflow.entity.warehouse.WarehouseLocation;
import com.orderflow.mapper.WarehouseLocationMapper;
import com.orderflow.service.WarehouseLocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/locations")
@PreAuthorize("hasAnyRole('ADMIN','INVENTORY_MANAGER','WAREHOUSE_OPERATOR')")
public class WarehouseLocationController {

    @Autowired
    private WarehouseLocationService warehouseLocationService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','WAREHOUSE_OPERATOR')")
    public ResponseEntity<WarehouseLocationDto> addWarehouseLocation(@RequestBody WarehouseLocationDto warehouseLocationDto) {
        return new ResponseEntity<>(warehouseLocationService.addWarehouseLocation(warehouseLocationDto), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<WarehouseLocationDto> getWarehouseLocationById(@PathVariable UUID id) {
        return new ResponseEntity<>(warehouseLocationService.getWarehouseLocationById(id), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteWarehouseLocationById(@PathVariable UUID id) {
        warehouseLocationService.deleteWarehouseLocationById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/{id}/activate")
    @PreAuthorize("hasAnyRole('ADMIN','WAREHOUSE_OPERATOR')")
    public ResponseEntity<WarehouseLocationDto> activateWarehouseLocation(@PathVariable UUID id) {
        return new ResponseEntity<>(warehouseLocationService.activateWarehouseLocationById(id), HttpStatus.OK);
    }

    @PutMapping("/{id}/deactivate")
    @PreAuthorize("hasAnyRole('ADMIN','WAREHOUSE_OPERATOR')")
    public ResponseEntity<WarehouseLocationDto> deactivateWarehouseLocation(@PathVariable UUID id) {
        return new ResponseEntity<>(warehouseLocationService.deactivateWarehouseLocationById(id), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Page<WarehouseLocationDto>> getAllWarehouseLocations(@PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(warehouseLocationService.getAllWarehouseLocations(pageable));
    }

    @GetMapping("/warehouse/{id}")
    public ResponseEntity<Page<WarehouseLocationDto>> getWarehouseLocationByWarehouseId(@PathVariable UUID id, @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(warehouseLocationService.getAllLocationsFromWarehouse(id, pageable));
    }

    @GetMapping("/{id}/children")
    public ResponseEntity<Page<WarehouseLocationDto>> getChildren(@PathVariable UUID id, @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(warehouseLocationService.getChildrenLocations(id, pageable));
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<Page<WarehouseLocationDto>> getWarehouseLocationsByType(@PathVariable WarehouseLocation.WarehouseLocationType type, @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(warehouseLocationService.getAllLocationFromType(type, pageable));
    }

}
