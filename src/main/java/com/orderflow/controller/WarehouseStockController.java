package com.orderflow.controller;

import com.orderflow.dto.WarehouseStockDto;
import com.orderflow.service.WarehouseStockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/stocks")
@PreAuthorize("hasAnyRole('ADMIN','INVENTORY_MANAGER','WAREHOUSE_OPERATOR')")
public class WarehouseStockController {

    @Autowired
    private WarehouseStockService warehouseStockService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','WAREHOUSE_OPERATOR')")
    public ResponseEntity<WarehouseStockDto> addStock(@RequestBody WarehouseStockDto warehouseStockDto) {
        return new ResponseEntity<>(warehouseStockService.addWarehouseStock(warehouseStockDto), HttpStatus.CREATED);
    }

    @PutMapping("/{warehouseStockId}")
    @PreAuthorize("hasAnyRole('ADMIN','WAREHOUSE_OPERATOR')")
    public ResponseEntity<WarehouseStockDto> updateStock(@PathVariable UUID warehouseStockId, @RequestBody BigDecimal quantity) {
        return new ResponseEntity<>(warehouseStockService.updateStock(warehouseStockId, quantity), HttpStatus.OK);
    }

    @PutMapping("/transfer/{targetId}")
    @PreAuthorize("hasAnyRole('ADMIN','WAREHOUSE_OPERATOR')")
    public ResponseEntity<WarehouseStockDto> transferStock(@RequestBody UUID sourceId, @RequestBody BigDecimal quantity, @PathVariable UUID targetId) {
        return new ResponseEntity<>(warehouseStockService.transferStock(sourceId, targetId, quantity), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<WarehouseStockDto> getStockById(@PathVariable UUID id) {
        return new ResponseEntity<>(warehouseStockService.getWarehouseStockById(id), HttpStatus.OK);
    }

    @GetMapping("/location/{id}")
    public ResponseEntity<Page<WarehouseStockDto>> getStockByLocation(@PathVariable UUID id, @PageableDefault(size = 20)Pageable pageable){
        return new ResponseEntity<>(warehouseStockService.getWarehouseStockByLocation(id, pageable), HttpStatus.OK);
    }

    @GetMapping("/variant/{id}")
    public ResponseEntity<Page<WarehouseStockDto>> getStockByVariant(@PathVariable UUID id, @PageableDefault(size = 20) Pageable pageable){
        return new ResponseEntity<>(warehouseStockService.getWarehouseStockByVariant(id, pageable), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteStock(@PathVariable UUID id) {
        warehouseStockService.deleteWarehouseStock(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
