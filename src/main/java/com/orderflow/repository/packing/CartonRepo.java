package com.orderflow.repository.packing;

import com.orderflow.entity.packing.Carton;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CartonRepo extends JpaRepository<Carton, UUID> {

    List<Carton> findAllByOrderByCreatedAtDesc();

    List<Carton> findAllByOrder_OrderId(UUID orderOrderId);

    List<Carton> findAllByPacker_UserId(UUID packerUserId);

    List<Carton> findALlByWarehouse_WarehouseId(UUID warehouseWarehouseId);

    List<Carton> findAllByPicking_PickingId(UUID pickingPickingId);

    Carton findTopByOrderByCartonNumberDesc();
}
