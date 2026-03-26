package com.orderflow.dto;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WarehouseStockDto {

    private UUID stockId;
    private UUID warehouseLocationId;
    private UUID variantId;
    private Double totalQuantity;
    private Double quantityAvailableForSales;
}
