package com.orderflow.entity.user;

import com.orderflow.entity.warehouse.Warehouse;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID userId;
    private String name;
    @Column(unique = true, nullable = false)
    private String code;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FieldOfWork fieldOfWork;
    @ManyToOne(fetch = FetchType.LAZY)
    private Warehouse userWarehouse;
    private String address;
    private String city;
    private String contactNumber;
    private String contactTelephone;
    private String contactEmail;
    private BigDecimal salary;
    private boolean active;
    private Instant joinedAt;
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

    public enum FieldOfWork{
        ORDER_PROCESSOR, WAREHOUSE_WORKER, DRIVER, ADMIN
    }

}
