package com.orderflow.controller;

import com.orderflow.dto.CustomerDto;
import com.orderflow.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("api/customers")
@PreAuthorize("hasAnyRole('ADMIN','INVENTORY_MANAGER')")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @PostMapping
    public ResponseEntity<CustomerDto> addCustomer(@RequestBody CustomerDto customerDto){
        return new ResponseEntity<>(customerService.addCustomer(customerDto), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<Page<CustomerDto>> getAllCustomers(@PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(customerService.getAllCustomers(pageable));
    }

    @GetMapping("/code/{code}")
    public ResponseEntity<CustomerDto> getCustomerByCode(@PathVariable String code){
        return new ResponseEntity<>(customerService.getCustomerByCode(code), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerDto> getCustomerById(@PathVariable UUID id){
        return new ResponseEntity<>(customerService.getCustomerById(id), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomerDto> updateCustomer(@PathVariable UUID id, @RequestBody CustomerDto customerDto){
        return new ResponseEntity<>(customerService.updateCustomer(id, customerDto), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<?> deleteCustomer(@PathVariable UUID id){
        customerService.deleteCustomer(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
