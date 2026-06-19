package com.orderflow.controller;

import com.orderflow.dto.ShipmentDto;
import com.orderflow.service.ShipmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("/api/shipments")
@PreAuthorize("hasAnyRole('ADMIN','INVENTORY_MANAGER','WAREHOUSE_OPERATOR','DRIVER')")
public class ShipmentController {

    @Autowired
    private ShipmentService shipmentService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','WAREHOUSE_OPERATOR')")
    public ResponseEntity<ShipmentDto> addShipment() {
        return new ResponseEntity<>(shipmentService.addShipment(), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<ShipmentDto>> getAllShipments() {
        return new ResponseEntity<>(shipmentService.getAllShipments(), HttpStatus.OK);
    }

    @GetMapping("/{shipmentId}")
    public ResponseEntity<ShipmentDto> getShipmentById(@PathVariable UUID shipmentId) {
        return new ResponseEntity<>(shipmentService.getShipmentById(shipmentId), HttpStatus.OK);
    }

    @PatchMapping("/{shipmentId}/cartons/{cartonId}")
    @PreAuthorize("hasAnyRole('ADMIN','WAREHOUSE_OPERATOR')")
    public ResponseEntity<ShipmentDto> addCartonToShipment(@PathVariable UUID shipmentId, @PathVariable UUID cartonId) {
        return new ResponseEntity<>(shipmentService.addCartonToShipment(shipmentId, cartonId), HttpStatus.OK);
    }

    @DeleteMapping("/{shipmentId}/cartons/{cartonId}")
    @PreAuthorize("hasAnyRole('ADMIN','WAREHOUSE_OPERATOR')")
    public ResponseEntity<ShipmentDto> removeCartonFromShipment(@PathVariable UUID shipmentId, @PathVariable UUID cartonId) {
        return new ResponseEntity<>(shipmentService.removeCartonFromShipment(shipmentId, cartonId), HttpStatus.OK);
    }

    @PatchMapping("/{shipmentId}/dispatch")
    @PreAuthorize("hasAnyRole('ADMIN','WAREHOUSE_OPERATOR')")
    public ResponseEntity<ShipmentDto> dispatchShipment(@PathVariable UUID shipmentId) {
        return new ResponseEntity<>(shipmentService.dispatchShipment(shipmentId), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ADMIN','DRIVER')")
    @PatchMapping("/{shipmentId}/deliver")
    public ResponseEntity<ShipmentDto> deliverShipment(@PathVariable UUID shipmentId, @RequestBody Set<UUID> cartonIds) {
        return new ResponseEntity<>(shipmentService.deliverShipment(shipmentId, cartonIds), HttpStatus.OK);
    }

    @DeleteMapping("/{shipmentId}")
    public ResponseEntity<Void> deleteShipment(@PathVariable UUID shipmentId) {
        shipmentService.deleteShipment(shipmentId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}