package com.orderflow.entity.picking;

import com.orderflow.entity.user.User;
import com.orderflow.entity.warehouse.Warehouse;
import com.orderflow.entity.order.Order;
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
@NoArgsConstructor
@AllArgsConstructor
public class Picking {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID pickingId;
    @ManyToOne(fetch = FetchType.LAZY)
    private User picker;
    @ManyToOne(fetch = FetchType.LAZY)
    private Order order;
    @Enumerated(EnumType.STRING)
    private PickingStatus status;
    private Instant date;
    @ManyToOne(fetch = FetchType.LAZY)
    private Warehouse warehouse;
    @OneToMany(mappedBy = "picking", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<PickingItem> pickingItems = new HashSet<>();
    private BigDecimal totalItems;
    private Instant startedAt;
    private Instant completedAt;


    public enum PickingStatus {
        PENDING, PICKING, PICKED
    }

}
