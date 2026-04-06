package com.orderflow.dto;

import com.orderflow.entity.order.Order;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderDto {

    private UUID orderId;
    private String orderNumber;
    private UUID customerId;
    private String receiverAddress;
    private String receiverPhone;
    private String receiverName;
    private Order.OrderStatus status = Order.OrderStatus.PENDING;
    private Order.Priority priority;
    private Instant orderDate;
    private Order.PaymentStatus paymentStatus = Order.PaymentStatus.PENDING;
    private Set<OrderItemDto> items = new HashSet<>();
    private BigDecimal subtotal;
    private BigDecimal totalDiscount;
    private BigDecimal totalTax;
    private BigDecimal totalAmount;
    private UUID fulfillingWarehouseId;
    private UUID createdBy;
    private UUID confirmedBy;
    private Instant confirmedAt;
    private UUID assignedPicker;
    private UUID assignedPacker;
    private UUID assignedDispatcher;
    private UUID assignedDriver;
    private Instant createdAt;
}