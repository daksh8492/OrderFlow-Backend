package com.orderflow.dto;

import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PickingSummaryDto {

    private UUID pickingId;
    private UUID pickerId;
    private UUID orderId;
    private UUID warehouseId;
    private Instant createdAt;
}
