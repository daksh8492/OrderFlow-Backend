package com.orderflow.repository.picking;

import com.orderflow.entity.picking.PickingItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PickingItemRepo extends JpaRepository<PickingItem, UUID> {
}
