package com.orderflow.mapper;

import com.orderflow.dto.OrderDto;
import com.orderflow.entity.customer.Customer;
import com.orderflow.entity.order.Order;
import com.orderflow.entity.order.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;
import java.util.UUID;

@Mapper(componentModel = "spring", uses = OrderItem.class)
public interface OrderMapper {

    @Mapping(source = "customerId", target = "customer.customerId")
    @Mapping(target = "fulfillingWarehouse", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "confirmedBy", ignore = true)
    @Mapping(target = "confirmedAt", ignore = true)
    @Mapping(target = "assignedPicker", ignore = true)
    @Mapping(target = "assignedPacker", ignore = true)
    @Mapping(target = "assignedDispatcher", ignore = true)
    @Mapping(target = "assignedDriver", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "subtotal", ignore = true)
    @Mapping(target = "totalDiscount", ignore = true)
    @Mapping(target = "totalTax", ignore = true)
    @Mapping(target = "totalAmount", ignore = true)
    @Mapping(target = "items", ignore = true)
    Order orderDtoToOrder(OrderDto orderDto);

    @Mapping(source = "customer.customerId", target = "customerId")
    @Mapping(source = "fulfillingWarehouse.warehouseId", target = "fulfillingWarehouseId")
    @Mapping(source = "createdBy.userId", target = "createdBy")
    @Mapping(source = "confirmedBy.userId", target = "confirmedBy")
    @Mapping(source = "assignedPicker.userId", target = "assignedPicker")
    @Mapping(source = "assignedPacker.userId", target = "assignedPacker")
    @Mapping(source = "assignedDispatcher.userId", target = "assignedDispatcher")
    @Mapping(source = "assignedDriver.userId", target = "assignedDriver")
    OrderDto orderToOrderDto(Order order);

    List<OrderDto> ordersToOrderDtos(List<Order> orders);

    @Named("uuidToCustomer")
    default Customer uuidToCustomer(UUID customerId) {
        if (customerId == null) return null;
        Customer customer = new Customer();
        customer.setCustomerId(customerId);
        return customer;
    }

}
