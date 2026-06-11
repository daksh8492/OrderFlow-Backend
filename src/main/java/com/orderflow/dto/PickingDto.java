package com.orderflow.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PickingDto {

    private UUID pickingId;
    private UUID pickerId;
    private UUID orderId;
//    private Picking.PickingStatus status;
    private UUID warehouseId;
    private Set<PickingItemDto> pickingItems = new HashSet<>();
    private BigDecimal totalItems;
//    private Instant startedAt;
//    private Instant completedAt;
    private Instant createdAt;
}
