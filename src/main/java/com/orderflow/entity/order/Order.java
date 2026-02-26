package com.orderflow.entity.order;

import com.orderflow.entity.customer.Customer;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID orderId;
    private String orderNumber;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Customer customer;
    private String receiverAddress;
    private String receiverPhone;
    private String receiverName;
    @Enumerated(EnumType.STRING)
    private OrderStatus status = OrderStatus.PENDING;
    @Enumerated(EnumType.STRING)
    private Priority priority;
    private Instant orderDate;
    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus = PaymentStatus.PENDING;
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<OrderItem> items = new HashSet<>();
    private BigDecimal subtotal;
    private BigDecimal totalDiscount;
    private BigDecimal totalTax;
    private BigDecimal totalAmount;
    private Instant createdAt;
    private Instant updatedAt;


    @PrePersist
    void onCreate(){
        createdAt = Instant.now();
        updatedAt = Instant.now();
    }

    @PreUpdate
    void onUpdate(){
        updatedAt = Instant.now();
    }

    public void addItem(OrderItem item){
        items.add(item);
        item.setOrder(this);
    }

    public enum OrderStatus{
        PENDING, PROCESSING, PICKED, PACKED, SHIPPED, OUT_FOR_DELIVERY, DELIVERED, COMPLETED, CANCELLED, FAILED, RETURNED
    }

    public enum Priority {
        LOW, MEDIUM, HIGH, URGENT
    }

    public enum PaymentStatus {
        PENDING, PARTIALLY_PAID, PAID, REFUNDED
    }

}
