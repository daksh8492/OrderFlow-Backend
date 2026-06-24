package com.orderflow.repository.packing;

import com.orderflow.entity.packing.Carton;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CartonRepo extends JpaRepository<Carton, UUID> {

    Page<Carton> findAllByOrderByCreatedAtDesc(Pageable pageable);

    Page<Carton> findAllByOrder_OrderId(UUID orderOrderId, Pageable pageable);

    Page<Carton> findAllByPacker_UserId(UUID packerUserId, Pageable pageable);

    Page<Carton> findALlByWarehouse_WarehouseId(UUID warehouseWarehouseId, Pageable pageable);

    Page<Carton> findAllByPicking_PickingId(UUID pickingPickingId, Pageable pageable);

    List<Carton> findAllByOrder_OrderId(UUID orderOrderId);

    Carton findTopByOrderByCartonNumberDesc();
}
