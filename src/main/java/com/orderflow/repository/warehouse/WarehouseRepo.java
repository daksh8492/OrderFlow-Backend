package com.orderflow.repository.warehouse;

import com.orderflow.entity.warehouse.Warehouse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface WarehouseRepo extends JpaRepository<Warehouse, UUID> {
    boolean existsByCode(String code);

    Optional<Warehouse> findByCode(String code);
}
