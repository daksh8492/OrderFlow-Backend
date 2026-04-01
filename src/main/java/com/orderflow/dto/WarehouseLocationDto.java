package com.orderflow.dto;

import com.orderflow.entity.warehouse.Warehouse;
import com.orderflow.entity.warehouse.WarehouseLocation;
import lombok.*;
import java.util.UUID;


@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WarehouseLocationDto {

    private UUID locationId;
    private WarehouseLocation.WarehouseLocationType locationType;
    private UUID warehouseId;
    private String code;
    private UUID parentLocationId;
    private String locationName;
    private Boolean active;
}
