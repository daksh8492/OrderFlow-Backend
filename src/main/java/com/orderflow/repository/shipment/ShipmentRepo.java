package com.orderflow.repository.shipment;

import com.orderflow.entity.packing.Carton;
import com.orderflow.entity.shipment.Shipment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ShipmentRepo extends JpaRepository<Shipment, UUID> {

    List<Shipment> findAllByOrderByCreatedAtDesc();

    boolean existsByCartonsContaining(Carton carton);

    Shipment findTopByOrderByShipmentNumberDesc();
}
