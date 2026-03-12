package com.orderflow.entity.packing;

import com.orderflow.entity.shipment.Shipment;
import com.orderflow.entity.user.User;
import com.orderflow.entity.warehouse.Warehouse;
import com.orderflow.entity.order.Order;
import com.orderflow.entity.picking.Picking;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Carton {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID cartonId;
    private String cartonNumber;
    @ManyToOne(fetch = FetchType.LAZY)
    private Order order;
    @ManyToOne(fetch = FetchType.LAZY)
    private User packer;
    @ManyToOne(fetch = FetchType.LAZY)
    private Warehouse warehouse;
    private BigDecimal weight;
    @ManyToOne(fetch = FetchType.LAZY)
    private Picking picking;
    @OneToMany(mappedBy = "carton", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<CartonItem> cartonItems = new HashSet<>();
    @ManyToOne(fetch = FetchType.LAZY)
    private Shipment shipment;
    @Enumerated(EnumType.STRING)
    private CartonStatus status;
    private Instant createdAt;
    private Instant packedAt;

    @PrePersist
    void onCreate(){
        createdAt = Instant.now();
    }

    public enum CartonStatus {
        PACKING, PACKED, DELIVERED
    }

}
