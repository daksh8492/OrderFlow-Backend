package com.orderflow.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PickingItemDto {

    private UUID pickingItemId;
    private UUID pickingId;
    private UUID orderItemId;
    private BigDecimal pickedItems;
    private UUID warehouseStockId;
    private Instant createdAt;
}
