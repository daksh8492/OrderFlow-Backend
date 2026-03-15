package com.orderflow.repository.warehouse;

import com.orderflow.entity.warehouse.Warehouse;
import com.orderflow.entity.warehouse.WarehouseLocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface WarehouseLocationRepo extends JpaRepository<WarehouseLocation, UUID> {

    List<WarehouseLocation> findAllByWarehouseAndLocationTypeAndParentLocation(Warehouse warehouse, WarehouseLocation.WarehouseLocationType locationType, WarehouseLocation parentLocation);

    List<WarehouseLocation> findByWarehouse_WarehouseId(UUID warehouseWarehouseId);

    List<WarehouseLocation> findAllByParentLocation_LocationId(UUID parentLocationLocationId);

    List<WarehouseLocation> findAllByLocationType(WarehouseLocation.WarehouseLocationType locationType);

    Long countByWarehouseAndLocationTypeAndParentLocation(Warehouse warehouse, WarehouseLocation.WarehouseLocationType locationType, WarehouseLocation parentLocation);
}
