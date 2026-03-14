package com.orderflow.dto;

import com.orderflow.entity.warehouse.Warehouse;
import lombok.*;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WarehouseDto {

    private UUID warehouseId;
    private String name;
    private String code;
    private String address;
    private String city;
    private Warehouse.WarehouseStatus status;
    private Set<UUID> locations = new HashSet<>();
}
