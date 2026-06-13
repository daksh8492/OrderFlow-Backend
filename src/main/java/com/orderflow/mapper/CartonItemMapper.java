package com.orderflow.mapper;

import com.orderflow.dto.CartonItemDto;
import com.orderflow.entity.order.OrderItem;
import com.orderflow.entity.packing.Carton;
import com.orderflow.entity.packing.CartonItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.Set;
import java.util.UUID;

@Mapper(componentModel = "spring")
public interface CartonItemMapper {

    @Mapping(source = "cartonId", target = "carton", qualifiedByName = "uuidToCarton")
    @Mapping(source = "orderItemId", target = "orderItem", qualifiedByName = "uuidToOrderItem")
    @Mapping(target = "createdAt", ignore = true)
    CartonItem cartonItemDtoToCartonItem(CartonItemDto cartonItemDto);

    @Mapping(source = "carton.cartonId", target = "cartonId")
    @Mapping(source = "orderItem.orderItemId", target = "orderItemId")
    CartonItemDto cartonItemToCartonItemDto(CartonItem cartonItem);

    Set<CartonItemDto> cartonItemsToCartonItemDtos(Set<CartonItem> cartonItems);

    Set<CartonItem> cartonItemDtosToCartonItems(Set<CartonItemDto> cartonItemDtos);

    @Named("uuidToCarton")
    default Carton uuidToCarton(UUID cartonId) {
        if (cartonId == null) return null;
        Carton carton = new Carton();
        carton.setCartonId(cartonId);
        return carton;
    }

    @Named("uuidToOrderItem")
    default OrderItem uuidToOrderItem(UUID orderItemId) {
        if (orderItemId == null) return null;
        OrderItem orderItem = new OrderItem();
        orderItem.setOrderItemId(orderItemId);
        return orderItem;
    }
}
