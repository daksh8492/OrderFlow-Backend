package com.orderflow.controller;

import com.orderflow.dto.CartonDto;
import com.orderflow.service.CartonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/cartons")
public class CartonController {

    @Autowired
    private CartonService cartonService;

    @PostMapping()
    public ResponseEntity<CartonDto> addCarton(@RequestBody CartonDto cartonDto) {
        return new ResponseEntity<>(cartonService.addCarton(cartonDto), HttpStatus.CREATED);
    }

    @GetMapping()
    public ResponseEntity<List<CartonDto>> getCartons() {
        return new ResponseEntity<>(cartonService.getAllCartons(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CartonDto> getCartonById(@PathVariable UUID id) {
        return new ResponseEntity<>(cartonService.getCartonById(id), HttpStatus.OK);
    }

    @GetMapping("/orders/{orderId}")
    public ResponseEntity<List<CartonDto>> getCartonsByOrderId(@PathVariable UUID orderId) {
        return new ResponseEntity<>(cartonService.getCartonsByOrderId(orderId), HttpStatus.OK);
    }

    @GetMapping("/packer/{packerId}")
    public ResponseEntity<List<CartonDto>> getCartonsByPackerId(@PathVariable UUID packerId) {
        return new ResponseEntity<>(cartonService.getCartonsByPackerId(packerId), HttpStatus.OK);
    }

    @GetMapping("/warehouse/{warehouseId}")
    public ResponseEntity<List<CartonDto>> getCartonsByWarehouseId(@PathVariable UUID warehouseId) {
        return new ResponseEntity<>(cartonService.getCartonsByWarehouseId(warehouseId), HttpStatus.OK);
    }

    @GetMapping("/picking/{pickingId}")
    public ResponseEntity<List<CartonDto>> getCartonsByPickingId(@PathVariable UUID pickingId) {
        return new ResponseEntity<>(cartonService.getCartonsByPickingId(pickingId), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CartonDto> updateCarton(@PathVariable UUID id, @RequestBody CartonDto cartonDto) {
        return new ResponseEntity<>(cartonService.updateCarton(id, cartonDto), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCarton(@PathVariable UUID id) {
        cartonService.deleteCarton(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
