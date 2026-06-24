package com.orderflow.controller;

import com.orderflow.dto.CartonDto;
import com.orderflow.service.CartonService;
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
@RequestMapping("/api/cartons")
@PreAuthorize("hasAnyRole('ADMIN','INVENTORY_MANAGER','WAREHOUSE_OPERATOR')")
public class CartonController {

    @Autowired
    private CartonService cartonService;

    @PostMapping()
    @PreAuthorize("hasAnyRole('ADMIN', 'WAREHOUSE_OPERATOR')")
    public ResponseEntity<CartonDto> addCarton(@RequestBody CartonDto cartonDto) {
        return new ResponseEntity<>(cartonService.addCarton(cartonDto), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CartonDto> getCartonById(@PathVariable UUID id) {
        return new ResponseEntity<>(cartonService.getCartonById(id), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Page<CartonDto>> getCartons(@PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(cartonService.getAllCartons(pageable));
    }

    @GetMapping("/orders/{orderId}")
    public ResponseEntity<Page<CartonDto>> getCartonsByOrderId(@PathVariable UUID orderId, @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(cartonService.getCartonsByOrderId(orderId, pageable));
    }

    @GetMapping("/packer/{packerId}")
    public ResponseEntity<Page<CartonDto>> getCartonsByPackerId(@PathVariable UUID packerId, @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(cartonService.getCartonsByPackerId(packerId, pageable));
    }

    @GetMapping("/warehouse/{warehouseId}")
    public ResponseEntity<Page<CartonDto>> getCartonsByWarehouseId(@PathVariable UUID warehouseId, @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(cartonService.getCartonsByWarehouseId(warehouseId, pageable));
    }

    @GetMapping("/picking/{pickingId}")
    public ResponseEntity<Page<CartonDto>> getCartonsByPickingId(@PathVariable UUID pickingId, @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(cartonService.getCartonsByPickingId(pickingId, pageable));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'WAREHOUSE_OPERATOR')")
    public ResponseEntity<CartonDto> updateCarton(@PathVariable UUID id, @RequestBody CartonDto cartonDto) {
        return new ResponseEntity<>(cartonService.updateCarton(id, cartonDto), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCarton(@PathVariable UUID id) {
        cartonService.deleteCarton(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
