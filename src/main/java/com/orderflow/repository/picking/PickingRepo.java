package com.orderflow.repository.picking;

import com.orderflow.dto.PickingSummaryDto;
import com.orderflow.entity.picking.Picking;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PickingRepo extends JpaRepository<Picking, UUID> {

    Picking findByOrder_OrderId(UUID orderOrderId);

    List<Picking> findByPicker_UserId(UUID pickerUserId);

    List<Picking> findByWarehouse_WarehouseId(UUID warehouseWarehouseId);

    boolean existsByOrder_OrderId(UUID orderOrderId);

    Page<Picking> findByWarehouse_WarehouseId(UUID warehouseWarehouseId, Pageable pageable);

    Page<Picking> findByPicker_UserId(UUID pickerUserId, Pageable pageable);
}
