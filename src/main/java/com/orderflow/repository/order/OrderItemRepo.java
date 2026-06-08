package com.orderflow.repository.order;

import com.orderflow.entity.order.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Repository
public interface OrderItemRepo extends JpaRepository<OrderItem, UUID> {
    Set<OrderItem> findAllByOrder_OrderId(UUID orderOrderId);

    Optional<OrderItem> findByOrderItemIdAndOrder_OrderId(UUID orderItemId, UUID orderOrderId);
}
