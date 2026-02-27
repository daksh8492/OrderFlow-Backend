package com.orderflow.repository.warehouse;

import com.orderflow.entity.warehouse.WarehouseStock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface WarehouseStockRepo extends JpaRepository<WarehouseStock, UUID> {
}
