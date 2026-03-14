package com.orderflow.service;

import com.orderflow.entity.customer.Customer;
import com.orderflow.exceptions.CustomerNotFoundException;
import com.orderflow.repository.customer.CustomerRepo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepo customerRepo;

    public Customer addCustomer(Customer customer){
        Long count = customerRepo.count();
        String code;
        do {
            code = "CUST-"+ String.format("%04d", count+1);
            count++;
        }while (customerRepo.existsByCustomerCode(code));
        customer.setCustomerCode(code);
        return customerRepo.save(customer);
    }

    public List<Customer> getAllCustomers() {return customerRepo.findAll();}

    public Customer getCustomerByCode(String code){
        Optional<Customer> customer = customerRepo.findByCustomerCode(code);
        return customer.orElseThrow(()-> new CustomerNotFoundException("Customer not found"));
    }

    public Customer getCustomerById(UUID id){
        Optional<Customer> customer = customerRepo.findById(id);
        return customer.orElseThrow(()-> new CustomerNotFoundException("Customer not found"));
    }

    public Customer updateCustomer(UUID id, Customer customer){
        Customer existingCustomer = getCustomerById(id);
        BeanUtils.copyProperties(customer, existingCustomer, "customerCode", "customerId");
        return customerRepo.save(existingCustomer);
    }

    public void deleteCustomer(UUID id){
        Customer customer = getCustomerById(id);
        customerRepo.delete(customer);
    }
}
