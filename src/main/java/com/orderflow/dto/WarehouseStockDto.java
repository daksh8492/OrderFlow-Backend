package com.orderflow.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WarehouseStockDto {

    private UUID stockId;
    private UUID warehouseId;
    private UUID warehouseLocationId;
    private UUID variantId;
    private BigDecimal totalQuantity;
    private Instant createdAt;
}
