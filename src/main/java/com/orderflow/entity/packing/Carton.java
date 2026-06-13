package com.orderflow.entity.packing;

import com.orderflow.entity.picking.PickingItem;
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
    @Column(unique = true, nullable = false)
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
    @Enumerated(EnumType.STRING)
    private CartonStatus status;
    private Instant createdAt;
    private Instant updatedAt;

    @PrePersist
    void onCreate(){
        createdAt = Instant.now();
    }

    @PreUpdate
    void onUpdate() {
        updatedAt = Instant.now();
    }

    public void addItem(CartonItem item) {
        cartonItems.add(item);
        item.setCarton(this);
    }

    public enum CartonStatus {
        PACKED, SHIPPED, DELIVERED
    }

}
