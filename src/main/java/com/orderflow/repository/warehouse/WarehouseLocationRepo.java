package com.orderflow.repository.warehouse;

import com.orderflow.entity.warehouse.Warehouse;
import com.orderflow.entity.warehouse.WarehouseLocation;
import com.orderflow.entity.warehouse.WarehouseStock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface WarehouseLocationRepo extends JpaRepository<WarehouseLocation, UUID> {

    Page<WarehouseLocation> findByWarehouse_WarehouseId(UUID id, Pageable pageable);

    Page<WarehouseLocation> findAllByParentLocation_LocationId(UUID id, Pageable pageable);

    Page<WarehouseLocation> findAllByLocationType(
            WarehouseLocation.WarehouseLocationType type,
            Pageable pageable);

    Long countByWarehouseAndLocationTypeAndParentLocation(Warehouse warehouse, WarehouseLocation.WarehouseLocationType locationType, WarehouseLocation parentLocation);

    List<WarehouseStock> findAllByLocationId(UUID locationId);
}
