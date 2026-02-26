package com.orderflow.entity.shipment;


import com.orderflow.entity.user.User;
import com.orderflow.entity.packing.Carton;
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
    @OneToMany(mappedBy = "shipment", cascade = CascadeType.ALL)
    private Set<Carton> cartons = new HashSet<>();
    private String trackingNumber;
    @ManyToOne(fetch = FetchType.LAZY)
    private User shipper;
    @Enumerated(EnumType.STRING)
    private Status status;
    private Instant dispatchAt;
    private Instant deliveredAt;


    public enum Status {
        DOCKING, DEPARTED, COMPLETED
    }
}
