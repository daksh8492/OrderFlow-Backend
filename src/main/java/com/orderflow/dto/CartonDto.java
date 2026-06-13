package com.orderflow.dto;

import com.orderflow.entity.packing.Carton;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CartonDto {
    private UUID cartonId;
    private String cartonNumber;
    private UUID orderId;
    private UUID packerId;
    private UUID warehouseId;
    private BigDecimal weight;
    private UUID pickingId;
    private Set<CartonItemDto> cartonItems;
    private Carton.CartonStatus status;
    private Instant createdAt;
}
