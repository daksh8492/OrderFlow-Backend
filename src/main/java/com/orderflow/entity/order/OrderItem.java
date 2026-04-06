package com.orderflow.entity.order;


import com.orderflow.entity.product.Variant;
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
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID orderItemId;
    private Long serialId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "variant_id")
    private Variant variant;
    private BigDecimal rate;
    private BigDecimal quantity;
    private BigDecimal taxRate;
    private BigDecimal taxAmount;
    @Enumerated(EnumType.STRING)
    private DiscountType discountType;
    private BigDecimal discountValue;
    private BigDecimal discountAmount;
    private BigDecimal itemTotal;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;
    private Instant createdAt;
    private Instant updatedAt;

    @PrePersist
    void onCreate() {
        createdAt = Instant.now();
        updatedAt = Instant.now();
    }

    @PreUpdate
    void onUpdate() {
        updatedAt = Instant.now();
    }

    public enum DiscountType {
        VALUE, PERCENTAGE
    }
}
