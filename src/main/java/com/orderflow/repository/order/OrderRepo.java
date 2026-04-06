package com.orderflow.repository.order;

import com.orderflow.entity.order.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrderRepo extends JpaRepository<Order, UUID> {
    List<Order> findAllByCustomer_CustomerId(UUID customerCustomerId);

    Optional<Order> findByOrderNumber(String orderNumber);

    List<Order> findByStatus(Order.OrderStatus status);
}
