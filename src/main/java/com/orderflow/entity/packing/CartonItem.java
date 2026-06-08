package com.orderflow.entity.packing;


import com.orderflow.entity.order.OrderItem;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CartonItem {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID cartonItemId;
    @ManyToOne(fetch = FetchType.LAZY)
    private Carton carton;
    @ManyToOne(fetch = FetchType.LAZY)
    private OrderItem orderItem;
    private BigDecimal packedQuantity;
    private Instant createdAt;
    private Instant updatedAt;

}
