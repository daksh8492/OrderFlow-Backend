package com.orderflow.entity.order;

import com.orderflow.entity.customer.Customer;
import com.orderflow.entity.user.User;
import com.orderflow.entity.warehouse.Warehouse;
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
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID orderId;
    @Column(unique = true, nullable = false)
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
    @Column(precision = 19, scale = 4)
    private BigDecimal subtotal;
    @Column(precision = 19, scale = 4)
    private BigDecimal totalDiscount;
    @Column(precision = 19, scale = 4)
    private BigDecimal totalTax;
    @Column(precision = 19, scale = 4)
    private BigDecimal totalAmount;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "warehouse_id")
    private Warehouse fulfillingWarehouse;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    private User createdBy;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "confirmed_by")
    private User confirmedBy;
    private Instant confirmedAt;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "picker_id")
    private User assignedPicker;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "packer_id")
    private User assignedPacker;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dispatcher_id")
    private User assignedDispatcher;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "driver_id")
    private User assignedDriver;
    private Instant createdAt;
    private Instant updatedAt;

    @PrePersist
    void onCreate() {
        createdAt = Instant.now();
        updatedAt = Instant.now();
        orderDate = Instant.now();
    }

    @PreUpdate
    void onUpdate() {
        updatedAt = Instant.now();
    }

    public void addItem(OrderItem item) {
        items.add(item);
        item.setOrder(this);
    }

    public enum OrderStatus {
        PENDING, PROCESSING, PICKING, PICKED, PACKING, PACKED, DISPATCHING, SHIPPED, OUT_FOR_DELIVERY, DELIVERED, COMPLETED, CANCELLED, FAILED, RETURNED
    }

    public enum Priority {
        LOW, MEDIUM, HIGH, URGENT
    }

    public enum PaymentStatus {
        PENDING, PARTIALLY_PAID, PAID, REFUNDED
    }
}