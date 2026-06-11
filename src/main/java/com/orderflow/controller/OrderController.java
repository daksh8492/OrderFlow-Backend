package com.orderflow.controller;

import com.orderflow.dto.OrderDto;
import com.orderflow.dto.OrderItemDto;
import com.orderflow.dto.OrderSummaryDto;
import com.orderflow.entity.order.Order;
import com.orderflow.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderDto> addOrder(@RequestBody OrderDto orderDto) {
        return new ResponseEntity<>(orderService.addOrder(orderDto), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<OrderSummaryDto>> getAllOrders() {
        return new ResponseEntity<>(orderService.getAllOrders(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDto> getOrderById(@PathVariable UUID id) {
        return new ResponseEntity<>(orderService.getOrderById(id), HttpStatus.OK);
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<OrderSummaryDto>> getOrdersByCustomerId(@PathVariable UUID customerId) {
        return new ResponseEntity<>(orderService.getOrdersByCustomerId(customerId), HttpStatus.OK);
    }

    @GetMapping("/order_number/{orderNumber}")
    public ResponseEntity<OrderDto> getOrderByOrderNumber(@PathVariable String orderNumber) {
        return new ResponseEntity<>(orderService.getOrderbyOrderNumber(orderNumber), HttpStatus.OK);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<OrderSummaryDto>> getOrdersByStatus(@PathVariable Order.OrderStatus status) {
        return new ResponseEntity<>(orderService.getALlByStatus(status), HttpStatus.OK);
    }

    @PutMapping("/{orderId}")
    public ResponseEntity<OrderDto> updateOrder(@PathVariable UUID orderId, @RequestBody OrderDto orderDto){
        return new ResponseEntity<>(orderService.updateOrder(orderId, orderDto), HttpStatus.OK);
    }

    @PatchMapping("/{orderId}/status/{status}")
    public ResponseEntity<OrderDto> updateOrderStatus(@PathVariable UUID orderId, @PathVariable Order.OrderStatus status) {
        return new ResponseEntity<>(orderService.updateStatus(orderId, status), HttpStatus.OK);
    }

    @PatchMapping("/{orderId}/payment_status/{status}")
    public ResponseEntity<OrderDto> updatePaymentStatus(@PathVariable UUID orderId, @PathVariable Order.PaymentStatus status) {
        return new ResponseEntity<>(orderService.updatePaymentStatus(orderId, status), HttpStatus.OK);
    }

    @PatchMapping("/{orderId}/cancel")
    public ResponseEntity<OrderDto> cancelOrder(@PathVariable UUID orderId) {
        return new ResponseEntity<>(orderService.cancelOrder(orderId), HttpStatus.OK);
    }

    @DeleteMapping("/{orderId}")
    public ResponseEntity<?> deleteOrder(@PathVariable UUID orderId) {
        orderService.deleteOrder(orderId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
