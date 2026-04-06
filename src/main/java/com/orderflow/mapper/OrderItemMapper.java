package com.orderflow.mapper;

import com.orderflow.dto.OrderDto;
import com.orderflow.dto.OrderItemDto;
import com.orderflow.entity.order.OrderItem;
import com.orderflow.entity.product.Variant;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.Set;
import java.util.UUID;

@Mapper(componentModel = "spring")
public interface OrderItemMapper {

    @Mapping(source = "variantId", target = "variant", qualifiedByName = "uuidToVariant")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "order", ignore = true)
    @Mapping(target = "taxAmount", ignore = true)
    @Mapping(target = "discountAmount", ignore = true)
    @Mapping(target = "itemTotal", ignore = true)
    OrderItem orderItemDtoToOrderItem(OrderItemDto orderItemDto);

    @Mapping(source = "variant.variantId", target = "variantId")
    @Mapping(source = "order.orderId", target = "orderId")
    OrderItemDto orderItemToOrderDto(OrderItem orderItem);

    Set<OrderItemDto> orderItemsToOrderItemDtos(Set<OrderItem> orderItems);

    Set<OrderItem> orderItemDtosToOrderItems(Set<OrderItemDto> orderItemDtos);


    @Named("uuidToVariant")
    default Variant uuidToVariant(UUID variantId) {
        if (variantId == null) return null;
        Variant variant = new Variant();
        variant.setVariantId(variantId);
        return variant;
    }

}
