package com.orderflow.service;

import com.orderflow.dto.OrderDto;
import com.orderflow.dto.OrderItemDto;
import com.orderflow.entity.order.Order;
import com.orderflow.entity.order.OrderItem;
import com.orderflow.exceptions.*;
import com.orderflow.mapper.OrderItemMapper;
import com.orderflow.mapper.OrderMapper;
import com.orderflow.repository.customer.CustomerRepo;
import com.orderflow.repository.order.OrderItemRepo;
import com.orderflow.repository.order.OrderRepo;
import com.orderflow.repository.product.VariantRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class OrderService {

    @Autowired
    private OrderRepo orderRepo;

    @Autowired
    private OrderItemRepo orderItemRepo;

    @Autowired
    private CustomerRepo customerRepo;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderItemMapper orderItemMapper;

    @Autowired
    private VariantRepo variantRepo;

    @Transactional
    public OrderDto addOrder(OrderDto orderDto) {
        Order order = orderMapper.orderDtoToOrder(orderDto);
        order.setCustomer(customerRepo.findById(orderDto.getCustomerId()).orElseThrow(() -> new CustomerNotFoundException("Customer Not Found")));

        Set<OrderItem> items = orderItemMapper.orderItemDtosToOrderItems(orderDto.getItems());

        BigDecimal subtotal = BigDecimal.ZERO;
        BigDecimal totalDiscount = BigDecimal.ZERO;
        BigDecimal totalTax = BigDecimal.ZERO;

        for (OrderItem item : items) {
            item.setVariant(variantRepo.findById(item.getVariant().getVariantId()).orElseThrow(() -> new VariantNotFoundException("Variant Not Found")));

            BigDecimal gross = item.getRate().multiply(item.getQuantity());

            BigDecimal disc = BigDecimal.ZERO;
            if (item.getDiscountType() != null && item.getDiscountValue() != null) disc = item.getDiscountType() == OrderItem.DiscountType.PERCENTAGE ? gross.multiply(item.getDiscountValue()).divide(BigDecimal.valueOf(100), 4, RoundingMode.HALF_UP) : item.getDiscountValue();

            BigDecimal tax = BigDecimal.ZERO;
            if (item.getTaxRate() != null) tax = gross.subtract(disc).multiply(item.getTaxRate()).divide(BigDecimal.valueOf(100), 4, RoundingMode.HALF_UP);

            item.setDiscountAmount(disc);
            item.setTaxAmount(tax);
            item.setItemTotal(gross.subtract(disc).add(tax));

            subtotal = subtotal.add(gross);
            totalDiscount = totalDiscount.add(disc);
            totalTax = totalTax.add(tax);

            order.addItem(item);
        }

        order.setSubtotal(subtotal);
        order.setTotalDiscount(totalDiscount);
        order.setTotalTax(totalTax);
        order.setTotalAmount(subtotal.subtract(totalDiscount).add(totalTax));

        orderRepo.save(order);
        return orderMapper.orderToOrderDto(order);
    }

    @Transactional
    public OrderDto getOrderById(UUID orderId) {
        return orderMapper.orderToOrderDto(orderRepo.findById(orderId).orElseThrow( () -> new OrderNotFoundException("Order not found")));
    }

    @Transactional
    public OrderDto updateOrder(UUID orderId, OrderDto orderDto) {
        Order order = orderRepo.findById(orderId).orElseThrow(() -> new OrderNotFoundException("Order Not Found"));

        if (orderDto.getReceiverName() != null)    order.setReceiverName(orderDto.getReceiverName());
        if (orderDto.getReceiverAddress() != null) order.setReceiverAddress(orderDto.getReceiverAddress());
        if (orderDto.getReceiverPhone() != null)   order.setReceiverPhone(orderDto.getReceiverPhone());
        if (orderDto.getStatus() != null)          order.setStatus(orderDto.getStatus());
        if (orderDto.getPriority() != null)        order.setPriority(orderDto.getPriority());
        if (orderDto.getPaymentStatus() != null)   order.setPaymentStatus(orderDto.getPaymentStatus());

        if (orderDto.getItems() != null && !orderDto.getItems().isEmpty()) {

            Map<UUID, OrderItem> existingItemsMap = order.getItems().stream().collect(Collectors.toMap(OrderItem::getOrderItemId, i -> i));

            Set<UUID> incomingIds = new HashSet<>();

            for (OrderItemDto itemDto : orderDto.getItems()) {

                if (itemDto.getOrderItemId() != null && existingItemsMap.containsKey(itemDto.getOrderItemId())) {
                    OrderItem existing = existingItemsMap.get(itemDto.getOrderItemId());

                    if (itemDto.getRate() == null || itemDto.getQuantity() == null) throw new IllegalArgumentException("Rate and quantity are required for item update");

                    if (itemDto.getSerialId() != null)      existing.setSerialId(itemDto.getSerialId());
                    if (itemDto.getRate() != null)          existing.setRate(itemDto.getRate());
                    if (itemDto.getQuantity() != null)      existing.setQuantity(itemDto.getQuantity());
                    if (itemDto.getTaxRate() != null)       existing.setTaxRate(itemDto.getTaxRate());
                    if (itemDto.getVariantId() != null) existing.setVariant(variantRepo.findById(itemDto.getVariantId()).orElseThrow(() -> new VariantNotFoundException("Variant Not Found")));

                    if (itemDto.getDiscountType() != null || itemDto.getDiscountValue() != null) {
                        existing.setDiscountType(itemDto.getDiscountType());
                        existing.setDiscountValue(itemDto.getDiscountValue());
                    }

                    incomingIds.add(existing.getOrderItemId());
                } else {
                    OrderItem newItem = orderItemMapper.orderItemDtoToOrderItem(itemDto);
                    newItem.setVariant(variantRepo.findById(itemDto.getVariantId()).orElseThrow(() -> new VariantNotFoundException("Variant Not Found")));
                    order.addItem(newItem);
                    incomingIds.add(newItem.getOrderItemId());
                }
            }

            order.getItems().removeIf(item -> !incomingIds.contains(item.getOrderItemId()));

            BigDecimal subtotal      = BigDecimal.ZERO;
            BigDecimal totalDiscount = BigDecimal.ZERO;
            BigDecimal totalTax      = BigDecimal.ZERO;

            for (OrderItem item : order.getItems()) {
                BigDecimal gross = item.getRate().multiply(item.getQuantity());

                BigDecimal disc = BigDecimal.ZERO;
                if (item.getDiscountType() != null && item.getDiscountValue() != null) disc = item.getDiscountType() == OrderItem.DiscountType.PERCENTAGE ? gross.multiply(item.getDiscountValue()).divide(BigDecimal.valueOf(100), 4, RoundingMode.HALF_UP) : item.getDiscountValue();

                BigDecimal tax = BigDecimal.ZERO;
                if (item.getTaxRate() != null) tax = gross.subtract(disc).multiply(item.getTaxRate()).divide(BigDecimal.valueOf(100), 4, RoundingMode.HALF_UP);

                item.setDiscountAmount(disc);
                item.setTaxAmount(tax);
                item.setItemTotal(gross.subtract(disc).add(tax));

                subtotal      = subtotal.add(gross);
                totalDiscount = totalDiscount.add(disc);
                totalTax      = totalTax.add(tax);
            }

            order.setSubtotal(subtotal);
            order.setTotalDiscount(totalDiscount);
            order.setTotalTax(totalTax);
            order.setTotalAmount(subtotal.subtract(totalDiscount).add(totalTax));
        }

        orderRepo.save(order);
        return orderMapper.orderToOrderDto(order);
    }

    @Transactional
    public List<OrderDto> getAllOrders() {
        return orderMapper.ordersToOrderDtos(orderRepo.findAll());
    }

    @Transactional
    public List<OrderDto> getOrdersByCustomerId(UUID customerId) {
        return orderMapper.ordersToOrderDtos(orderRepo.findAllByCustomer_CustomerId(customerId));
    }

    @Transactional
    public OrderDto getOrderbyOrderNumber(String orderNumber) {
        return orderMapper.orderToOrderDto(orderRepo.findByOrderNumber(orderNumber).orElseThrow( () -> new OrderNotFoundException("Order not found")));
    }

    @Transactional
    public OrderDto cancelOrder(UUID orderId) {
        Order order = orderRepo.findById(orderId).orElseThrow( () -> new OrderNotFoundException("Order not found"));
        order.setStatus(Order.OrderStatus.CANCELLED);
        return orderMapper.orderToOrderDto(orderRepo.save(order));
    }

    @Transactional
    public OrderDto updateStatus(UUID orderId, Order.OrderStatus status){
        Order order = orderRepo.findById(orderId).orElseThrow( () -> new OrderNotFoundException("Order not found"));
        if (status == null) throw new IllegalArgumentException("Status is not valid");
        order.setStatus(status);
        return orderMapper.orderToOrderDto(orderRepo.save(order));
    }

    @Transactional
    public OrderDto updatePaymentStatus(UUID orderId, Order.PaymentStatus status){
        Order order = orderRepo.findById(orderId).orElseThrow( () -> new OrderNotFoundException("Order not found"));
        if (status == null) throw new IllegalArgumentException("Payment Status is not valid");
        order.setPaymentStatus(status);
        return orderMapper.orderToOrderDto(orderRepo.save(order));
    }

    @Transactional
    public Set<OrderItemDto> getOrderItemsByOrderId(UUID orderId) {
        return orderItemMapper.orderItemsToOrderItemDtos(orderItemRepo.findAllByOrder_OrderId(orderId));
    }

    @Transactional
    public OrderItemDto getOrderItemByOrderAndItemId(UUID orderId, UUID itemId) {
        return orderItemMapper.orderItemToOrderDto(orderItemRepo.findById(itemId).orElseThrow(() -> new OrderItemNotFoundException("Order Item not found")));
    }

    @Transactional
    public List<OrderDto> getALlByStatus(Order.OrderStatus status) {
        return orderMapper.ordersToOrderDtos(orderRepo.findByStatus(status));
    }



}
