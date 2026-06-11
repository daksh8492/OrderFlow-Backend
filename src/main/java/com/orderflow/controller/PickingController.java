package com.orderflow.controller;

import com.orderflow.dto.PickingDto;
import com.orderflow.dto.PickingSummaryDto;
import com.orderflow.service.PickingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/pickings")
public class PickingController {

    @Autowired
    private PickingService pickingService;

    @PostMapping
    public ResponseEntity<PickingDto> addPicking(@RequestBody PickingDto pickingDto) {
        return new ResponseEntity<>(pickingService.addPicking(pickingDto), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<PickingSummaryDto>> getAllPickings() {
        return new ResponseEntity<>(pickingService.getAllPickings(), HttpStatus.OK);
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
    public ResponseEntity<List<PickingSummaryDto>> getPickingByPicker(@PathVariable UUID pickerId) {
        return new ResponseEntity<>(pickingService.getPickingByPicker(pickerId), HttpStatus.OK);
    }

    @GetMapping("/warehouse/{warehouseId}")
    public ResponseEntity<List<PickingSummaryDto>> getPickingByWarehouse(@PathVariable UUID warehouseId) {
        return new ResponseEntity<>(pickingService.getPickingByWarehouse(warehouseId), HttpStatus.OK);
    }

    @PutMapping("/{pickingId}")
    public ResponseEntity<PickingDto> updatePicking(@PathVariable UUID pickingId, @RequestBody PickingDto pickingDto) {
        return new ResponseEntity<>(pickingService.updatePicking(pickingId, pickingDto), HttpStatus.OK);
    }

    @DeleteMapping("/{pickingId}")
    public ResponseEntity<Void> deletePicking(@PathVariable UUID pickingId) {
        pickingService.deletePicking(pickingId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}