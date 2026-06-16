package com.orderflow.dto;

import com.orderflow.entity.shipment.Shipment;
import lombok.*;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShipmentDto {

    private UUID shipmentId;
    private String shipmentNumber;
    private Set<UUID> cartonIds = new HashSet<>();
    private UUID warehouseId;
    private String trackingNumber;
    private UUID shipperId;
    private Shipment.Status status;
    private Instant dispatchAt;
    private Instant deliveredAt;
    private Instant createdAt;
}
