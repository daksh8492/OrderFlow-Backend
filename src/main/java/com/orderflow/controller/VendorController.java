package com.orderflow.controller;

import com.orderflow.dto.VendorDto;
import com.orderflow.entity.vendor.Vendor;
import com.orderflow.mapper.VendorMapper;
import com.orderflow.service.VendorService;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/vendors")
public class VendorController {

    @Autowired
    private VendorService vendorService;

    @PostMapping
    public ResponseEntity<VendorDto> createVendor(@RequestBody VendorDto vendorDto){
        return new ResponseEntity<>(vendorService.addVendor(vendorDto), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<VendorDto>> getAllVendors(){
        return new ResponseEntity<>(vendorService.getAllVendors(), HttpStatus.OK);
    }

    @GetMapping("/code/{code}")
    public ResponseEntity<VendorDto> getVendorByCode(@PathVariable String code){
        return new ResponseEntity<>(vendorService.getVendorByCode(code), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<VendorDto> getVendorById(@PathVariable UUID id){
        return new ResponseEntity<>(vendorService.getVendorById(id), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<VendorDto> updateVendor(@PathVariable UUID id, @RequestBody VendorDto vendorDto){
        return new ResponseEntity<>(vendorService.updateVendor(id, vendorDto), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteVendor(@PathVariable UUID id){
        vendorService.deleteVendor(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
