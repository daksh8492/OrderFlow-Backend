package com.orderflow.dto;

import com.orderflow.entity.customer.Customer;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CustomerDto {


    private UUID customerId;
    private String customerCode;
    private String customerName;
    private String address;
    private String city;
    private String contactNumber;
    private String contactEmail;
    private Customer.CustomerStatus status;
}
