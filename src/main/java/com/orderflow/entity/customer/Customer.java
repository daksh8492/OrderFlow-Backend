package com.orderflow.entity.customer;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID customerId;
    @Column(nullable = false, unique = true)
    private String customerCode;
    private String customerName;
    private String address;
    private String city;
    private String contactNumber;
    private String contactEmail;
    @Enumerated(EnumType.STRING)
    private CustomerStatus status;
    private Instant createdAt;
    private Instant updatedAt;

    @PrePersist
    void onCreate(){
        createdAt = Instant.now();
        updatedAt = Instant.now();
    }

    @PreUpdate
    void onUpdate(){
        updatedAt = Instant.now();
    }

    public enum CustomerStatus{
        INACTIVE, ACTIVE, CLOSED
    }
}
