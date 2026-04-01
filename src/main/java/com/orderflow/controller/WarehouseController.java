package com.orderflow.controller;

import com.orderflow.dto.WarehouseDto;
import com.orderflow.mapper.WarehouseMapper;
import com.orderflow.service.WarehouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/warehouses")
public class WarehouseController {

    @Autowired
    private WarehouseService warehouseService;

    @PostMapping
    public ResponseEntity<WarehouseDto> addWarehouse(@RequestBody WarehouseDto warehouseDto){
        return new ResponseEntity<>(warehouseService.addWarehouse(warehouseDto), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<WarehouseDto>> getAllWarehouses(){
        return new ResponseEntity<>(warehouseService.getAllWarehouses(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<WarehouseDto> getWarehouseById(@PathVariable UUID id){
        return new ResponseEntity<>(warehouseService.getWarehouseById(id), HttpStatus.OK);
    }

    @GetMapping("/code/{code}")
    public ResponseEntity<WarehouseDto> getWarehouseByCode(@PathVariable String code){
        return new ResponseEntity<>(warehouseService.getWarehouseByCode(code), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<WarehouseDto> updateWarehouse(@PathVariable UUID id, @RequestBody WarehouseDto warehouseDto){
        return new ResponseEntity<>(warehouseService.updateWarehouse(id, warehouseDto), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteWarehouse(@PathVariable UUID id){
        warehouseService.deleteWarehouse(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
