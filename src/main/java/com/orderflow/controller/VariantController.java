package com.orderflow.controller;

import com.orderflow.dto.VariantDto;
import com.orderflow.mapper.VariantMapper;
import com.orderflow.service.VariantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/variants")
public class VariantController {

    @Autowired
    private VariantService variantService;

    @PostMapping
    public ResponseEntity<VariantDto> addVariant(@RequestBody VariantDto variantDto){
        return new ResponseEntity<>(variantService.addVariant(variantDto), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<VariantDto>> getAllVariants(){
        return new ResponseEntity<>(variantService.getAllVariants(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<VariantDto> getVariantById(@PathVariable UUID id){
        //Avail qty will be implemented later after picking module
        return new ResponseEntity<>(variantService.getVariantById(id), HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<VariantDto> updateVariant(@PathVariable UUID id, @RequestBody VariantDto variantDto){
        return new ResponseEntity<>(variantService.updateVariant(id, variantDto), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteVariant(@PathVariable UUID id){
        variantService.deleteVariant(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/sku/{sku}")
    public ResponseEntity<VariantDto> getVariantBySku(@PathVariable String sku){
        return new ResponseEntity<>(variantService.getVariantBySku(sku), HttpStatus.OK);
    }

    @GetMapping("/barcode/{barcode}")
    public ResponseEntity<VariantDto> getVariantByBarcode(@PathVariable String barcode){
        return new ResponseEntity<>(variantService.getVariantByBarcode(barcode), HttpStatus.OK);
    }

    @GetMapping("/item/{id}")
    public ResponseEntity<List<VariantDto>> getVariantsByItemId(@PathVariable UUID id){
        return new ResponseEntity<>(variantService.getVariantsByItemId(id), HttpStatus.OK);
    }

    @PatchMapping("/{id}/activate")
     public ResponseEntity<VariantDto> activateVariant(@PathVariable UUID id){
        return new ResponseEntity<>(variantService.activateVariant(id), HttpStatus.OK);
    }

    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<VariantDto> deactivateVariant(@PathVariable UUID id){
        return new ResponseEntity<>(variantService.deactivateVariant(id), HttpStatus.OK);
    }

    @PatchMapping("/{id}/discontinue")
    public ResponseEntity<VariantDto> discontinueVariant(@PathVariable UUID id){
        return new ResponseEntity<>(variantService.discontinueVariant(id), HttpStatus.OK);
    }
}
