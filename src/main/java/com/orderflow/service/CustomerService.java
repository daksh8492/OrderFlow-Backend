package com.orderflow.service;

import com.orderflow.dto.CustomerDto;
import com.orderflow.entity.customer.Customer;
import com.orderflow.exceptions.CustomerNotFoundException;
import com.orderflow.mapper.CustomerMapper;
import com.orderflow.repository.customer.CustomerRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepo customerRepo;
    @Autowired
    private CustomerMapper customerMapper;

    public CustomerDto addCustomer(CustomerDto customerDto){
        Customer customer = customerMapper.customerDtoToCustomer(customerDto);
        Long count = customerRepo.count();
        String code;
        do {
            code = "CUST-"+ String.format("%04d", count+1);
            count++;
        }while (customerRepo.existsByCustomerCode(code));
        customer.setCustomerCode(code);
        return customerMapper.customerToCustomerDto(customerRepo.save(customer));
    }

    public List<CustomerDto> getAllCustomers() {return customerMapper.customersToCustomerDtos(customerRepo.findAll());}

    public CustomerDto getCustomerByCode(String code){
        Optional<Customer> customer = customerRepo.findByCustomerCode(code);
        return customerMapper.customerToCustomerDto(customer.orElseThrow(()-> new CustomerNotFoundException("Customer not found")));
    }

    public CustomerDto getCustomerById(UUID id){
        Optional<Customer> customer = customerRepo.findById(id);
        return customerMapper.customerToCustomerDto(customer.orElseThrow(()-> new CustomerNotFoundException("Customer not found")));
    }

    public CustomerDto updateCustomer(UUID id, CustomerDto customerDto){
        Customer customer = customerMapper.customerDtoToCustomer(customerDto);
        Customer existingCustomer = customerMapper.customerDtoToCustomer(getCustomerById(id));
        if (customer.getCustomerName() != null) existingCustomer.setCustomerName(customer.getCustomerName());
        if (customer.getAddress() != null) existingCustomer.setAddress(customer.getAddress());
        if (customer.getCity() != null) existingCustomer.setCity(customer.getCity());
        if (customer.getContactNumber() != null) existingCustomer.setContactNumber(customer.getContactNumber());
        if (customer.getContactEmail() != null) existingCustomer.setContactEmail(customer.getContactEmail());
        if (customer.getStatus() != null) existingCustomer.setStatus(customer.getStatus());
        return customerMapper.customerToCustomerDto(customerRepo.save(existingCustomer));
    }

    public void deleteCustomer(UUID id){
        Customer customer = customerMapper.customerDtoToCustomer(getCustomerById(id));
        customerRepo.delete(customer);
    }
}
