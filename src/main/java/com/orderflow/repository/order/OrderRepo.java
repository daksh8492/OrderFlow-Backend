package com.orderflow.repository.order;

import com.orderflow.entity.order.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrderRepo extends JpaRepository<Order, UUID> {
    Page<Order> findAllByCustomer_CustomerId(UUID customerCustomerId, Pageable pageable);

    Optional<Order> findByOrderNumber(String orderNumber);

    Optional<Order> findTopByOrderByOrderNumberDesc();

    Page<Order> findAllByOrderByOrderNumberDesc(Pageable pageable);

    Page<Order> findByStatus(Order.OrderStatus status, Pageable pageable);
}
