package com.orderflow.repository.warehouse;

import com.orderflow.entity.product.Variant;
import com.orderflow.entity.warehouse.Warehouse;
import com.orderflow.entity.warehouse.WarehouseLocation;
import com.orderflow.entity.warehouse.WarehouseStock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface WarehouseStockRepo extends JpaRepository<WarehouseStock, UUID> {
    boolean existsByVariantAndWarehouseLocation(Variant variant, WarehouseLocation warehouseLocation);

    Optional<WarehouseStock> findByVariantAndWarehouseLocation(Variant variant, WarehouseLocation warehouseLocation);

    List<WarehouseStock> findByWarehouseLocation(WarehouseLocation warehouseLocation);

    List<WarehouseStock> findByVariantAndWarehouse(Variant variant, Warehouse warehouse);
}
