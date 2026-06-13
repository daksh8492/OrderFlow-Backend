package com.orderflow.mapper;

import com.orderflow.dto.OrderDto;
import com.orderflow.dto.OrderSummaryDto;
import com.orderflow.entity.customer.Customer;
import com.orderflow.entity.order.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;
import java.util.UUID;

@Mapper(componentModel = "spring", uses = OrderItemMapper.class)
public interface OrderMapper {

    @Mapping(source = "customerId", target = "customer", qualifiedByName = "uuidToCustomer")
    @Mapping(target = "fulfillingWarehouse", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "subtotal", ignore = true)
    @Mapping(target = "totalDiscount", ignore = true)
    @Mapping(target = "totalTax", ignore = true)
    @Mapping(target = "totalAmount", ignore = true)
//    @Mapping(target = "items", ignore = true)
    Order orderDtoToOrder(OrderDto orderDto);

    @Mapping(source = "customer.customerId", target = "customerId")
    @Mapping(source = "fulfillingWarehouse.warehouseId", target = "fulfillingWarehouseId")
    @Mapping(source = "createdBy.userId", target = "createdBy")
    OrderDto orderToOrderDto(Order order);

    List<OrderDto> ordersToOrderDtos(List<Order> orders);

    @Named("uuidToCustomer")
    default Customer uuidToCustomer(UUID customerId) {
        if (customerId == null) return null;
        Customer customer = new Customer();
        customer.setCustomerId(customerId);
        return customer;
    }

    @Mapping(source = "customer.customerId", target = "customerId")
    OrderSummaryDto orderToOrderSummaryDto(Order order);

    List<OrderSummaryDto> ordersToOrderSummaryDtos(List<Order> orders);

}
