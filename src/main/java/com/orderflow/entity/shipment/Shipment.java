package com.orderflow.entity.shipment;


import com.orderflow.entity.user.User;
import com.orderflow.entity.packing.Carton;
import com.orderflow.entity.warehouse.Warehouse;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Shipment {


    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID shipmentId;
    @Column(unique = true, nullable = false)
    private String shipmentNumber;
    @OneToMany()
    @JoinColumn(name = "shipment_id")
    private Set<Carton> cartons = new HashSet<>();
    @ManyToOne(fetch = FetchType.LAZY)
    private Warehouse warehouse;
    private String trackingNumber;
    @ManyToOne(fetch = FetchType.LAZY)
    private User shipper;
    @Enumerated(EnumType.STRING)
    private Status status;
    private Instant dispatchAt;
    private Instant deliveredAt;
    private Instant createdAt;
    private Instant updatedAt;

    public void addCarton(Carton carton){
        cartons.add(carton);
    }

    @PrePersist
    void onCreate() {
        createdAt = Instant.now();
        updatedAt = Instant.now();
    }

    @PreUpdate
    void onUpdate() {
        updatedAt = Instant.now();
    }

    public enum Status {
        DOCKING, IN_TRANSIT, DELIVERED
    }
}
