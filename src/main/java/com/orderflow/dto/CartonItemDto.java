package com.orderflow.dto;

import com.orderflow.entity.order.OrderItem;
import com.orderflow.entity.packing.Carton;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CartonItemDto {
    private UUID cartonItemId;
    private UUID cartonId;
    private UUID orderItemId;
    private BigDecimal packedQuantity;
    private Instant createdAt;
}
