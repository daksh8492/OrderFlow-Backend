package com.orderflow.dto;

import lombok.*;

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
    private Double totalQuantity;
    private Instant createdAt;
}
