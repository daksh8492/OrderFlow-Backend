package com.orderflow.dto;

import com.orderflow.entity.order.OrderItem;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemDto {

    private UUID orderItemId;
    private Long serialId;
    private UUID variantId;
    private BigDecimal rate;
    private BigDecimal quantity;
    private BigDecimal taxRate;
    private BigDecimal taxAmount;
    private OrderItem.DiscountType discountType;
    private BigDecimal discountValue;
    private BigDecimal discountAmount;
    private BigDecimal itemTotal;
}
