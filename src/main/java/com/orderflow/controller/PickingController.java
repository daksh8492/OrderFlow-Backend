package com.orderflow.controller;

import com.orderflow.dto.PickingDto;
import com.orderflow.dto.PickingSummaryDto;
import com.orderflow.service.PickingService;
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
@RequestMapping("/api/pickings")
@PreAuthorize("hasAnyRole('ADMIN','INVENTORY_MANAGER','WAREHOUSE_OPERATOR')")
public class PickingController {

    @Autowired
    private PickingService pickingService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'WAREHOUSE_OPERATOR')")
    public ResponseEntity<PickingDto> addPicking(@RequestBody PickingDto pickingDto) {
        return new ResponseEntity<>(pickingService.addPicking(pickingDto), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<Page<PickingSummaryDto>> getAllPickings(@PageableDefault(size = 20)Pageable pageable) {
        return new ResponseEntity<>(pickingService.getAllPickings(pageable), HttpStatus.OK);
    }

    @GetMapping("/{pickingId}")
    public ResponseEntity<PickingDto> getPickingById(@PathVariable UUID pickingId) {
        return new ResponseEntity<>(pickingService.getPickingById(pickingId), HttpStatus.OK);
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<PickingDto> getPickingByOrder(@PathVariable UUID orderId) {
        return new ResponseEntity<>(pickingService.getPickingByOrder(orderId), HttpStatus.OK);
    }

    @GetMapping("/picker/{pickerId}")
    public ResponseEntity<Page<PickingSummaryDto>> getPickingByPicker(@PathVariable UUID pickerId, @PageableDefault(size = 20) Pageable pageable) {
        return new ResponseEntity<>(pickingService.getPickingByPicker(pickerId, pageable), HttpStatus.OK);
    }

    @GetMapping("/warehouse/{warehouseId}")
    public ResponseEntity<Page<PickingSummaryDto>> getPickingByWarehouse(@PathVariable UUID warehouseId, @PageableDefault(size = 20) Pageable pageable) {
        return new ResponseEntity<>(pickingService.getPickingByWarehouse(warehouseId, pageable), HttpStatus.OK);
    }

    @PutMapping("/{pickingId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'WAREHOUSE_OPERATOR')")
    public ResponseEntity<PickingDto> updatePicking(@PathVariable UUID pickingId, @RequestBody PickingDto pickingDto) {
        return new ResponseEntity<>(pickingService.updatePicking(pickingId, pickingDto), HttpStatus.OK);
    }

    @DeleteMapping("/{pickingId}")
    public ResponseEntity<Void> deletePicking(@PathVariable UUID pickingId) {
        pickingService.deletePicking(pickingId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}