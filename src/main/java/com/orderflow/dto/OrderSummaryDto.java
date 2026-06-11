package com.orderflow.dto;

import com.orderflow.entity.order.Order;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderSummaryDto {

    private UUID orderId;
    private String orderNumber;

    private UUID customerId;
    private String receiverName;

    private Order.OrderStatus status;
    private Order.Priority priority;
    private Instant orderDate;
    private Order.PaymentStatus paymentStatus;

    private BigDecimal totalAmount;
}
