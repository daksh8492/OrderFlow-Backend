package com.orderflow.controller;

import com.orderflow.dto.WarehouseStockDto;
import com.orderflow.service.WarehouseStockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/stocks")
public class WarehouseStockController {

    @Autowired
    private WarehouseStockService warehouseStockService;

    @PostMapping
    public ResponseEntity<WarehouseStockDto> addStock(@RequestBody WarehouseStockDto warehouseStockDto) {
        return new ResponseEntity<>(warehouseStockService.addWarehouseStock(warehouseStockDto), HttpStatus.CREATED);
    }

    @PutMapping("/{warehouseStockId}")
    public ResponseEntity<WarehouseStockDto> updateStock(@PathVariable UUID warehouseStockId, @RequestBody Double quantity) {
        return new ResponseEntity<>(warehouseStockService.updateStock(warehouseStockId, quantity), HttpStatus.OK);
    }

    @PutMapping("/transfer/{targetId}")
    public ResponseEntity<WarehouseStockDto> transferStock(@RequestBody UUID sourceId,@RequestBody Double quantity, @PathVariable UUID targetId) {
        return new ResponseEntity<>(warehouseStockService.transferStock(sourceId, targetId, quantity), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<WarehouseStockDto> getStockById(@PathVariable UUID id) {
        return new ResponseEntity<>(warehouseStockService.getWarehouseStockById(id), HttpStatus.OK);
    }

    @GetMapping("/location/{id}")
    public ResponseEntity<List<WarehouseStockDto>> getStockByLocation(@PathVariable UUID id){
        return new ResponseEntity<>(warehouseStockService.getWarehouseStockByLocation(id), HttpStatus.OK);
    }

    @GetMapping("/variant/{id}")
    public ResponseEntity<List<WarehouseStockDto>> getStockByVariant(@PathVariable UUID id){
        return new ResponseEntity<>(warehouseStockService.getWarehouseStockByVariant(id), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteStock(@PathVariable UUID id) {
        warehouseStockService.deleteWarehouseStock(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
