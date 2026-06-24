package com.orderflow.controller;

import com.orderflow.dto.OrderDto;
import com.orderflow.dto.OrderSummaryDto;
import com.orderflow.entity.order.Order;
import com.orderflow.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("api/orders")
@PreAuthorize("hasAnyRole('ADMIN','INVENTORY_MANAGER')")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderDto> addOrder(@RequestBody OrderDto orderDto) {
        return new ResponseEntity<>(orderService.addOrder(orderDto), HttpStatus.CREATED);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','INVENTORY_MANAGER','WAREHOUSE_OPERATOR')")
    public ResponseEntity<Page<OrderSummaryDto>> getAllOrders(@PageableDefault(size = 20)Pageable pageable) {
        return new ResponseEntity<>(orderService.getAllOrders(pageable), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','INVENTORY_MANAGER','WAREHOUSE_OPERATOR')")
    public ResponseEntity<OrderDto> getOrderById(@PathVariable UUID id) {
        return new ResponseEntity<>(orderService.getOrderById(id), HttpStatus.OK);
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<Page<OrderSummaryDto>> getOrdersByCustomerId(@PathVariable UUID customerId, @PageableDefault(size = 20) Pageable pageable) {
        return new ResponseEntity<>(orderService.getOrdersByCustomerId(customerId, pageable), HttpStatus.OK);
    }

    @GetMapping("/order-number/{orderNumber}")
    @PreAuthorize("hasAnyRole('ADMIN','INVENTORY_MANAGER','WAREHOUSE_OPERATOR')")
    public ResponseEntity<OrderDto> getOrderByOrderNumber(@PathVariable String orderNumber) {
        return new ResponseEntity<>(orderService.getOrderbyOrderNumber(orderNumber), HttpStatus.OK);
    }

    @GetMapping("/status/{status}")
    @PreAuthorize("hasAnyRole('ADMIN','INVENTORY_MANAGER','WAREHOUSE_OPERATOR')")
    public ResponseEntity<Page<OrderSummaryDto>> getOrdersByStatus(@PathVariable Order.OrderStatus status, @PageableDefault(size = 20) Pageable pageable) {
        return new ResponseEntity<>(orderService.getALlByStatus(status, pageable), HttpStatus.OK);
    }

    @PutMapping("/{orderId}")
    public ResponseEntity<OrderDto> updateOrder(@PathVariable UUID orderId, @RequestBody OrderDto orderDto){
        return new ResponseEntity<>(orderService.updateOrder(orderId, orderDto), HttpStatus.OK);
    }

    @PatchMapping("/{orderId}/status/{status}")
    @PreAuthorize("hasAnyRole('ADMIN','INVENTORY_MANAGER','WAREHOUSE_OPERATOR')")
    public ResponseEntity<OrderDto> updateOrderStatus(@PathVariable UUID orderId, @PathVariable Order.OrderStatus status) {
        return new ResponseEntity<>(orderService.updateStatus(orderId, status), HttpStatus.OK);
    }

    @PatchMapping("/{orderId}/payment-status/{status}")
    public ResponseEntity<OrderDto> updatePaymentStatus(@PathVariable UUID orderId, @PathVariable Order.PaymentStatus status) {
        return new ResponseEntity<>(orderService.updatePaymentStatus(orderId, status), HttpStatus.OK);
    }

    @PatchMapping("/{orderId}/cancel")
    public ResponseEntity<OrderDto> cancelOrder(@PathVariable UUID orderId) {
        return new ResponseEntity<>(orderService.cancelOrder(orderId), HttpStatus.OK);
    }

    @DeleteMapping("/{orderId}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<?> deleteOrder(@PathVariable UUID orderId) {
        orderService.deleteOrder(orderId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PatchMapping("/{orderId}/assign-warehouse/{warehouseId}")
    public ResponseEntity<OrderDto> assignWarehouse(@PathVariable UUID orderId, @PathVariable UUID warehouseId) {
        return new ResponseEntity<>(orderService.assignWarehouse(orderId, warehouseId), HttpStatus.OK);
    }

}
