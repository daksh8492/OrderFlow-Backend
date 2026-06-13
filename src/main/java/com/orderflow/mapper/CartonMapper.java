package com.orderflow.mapper;

import com.orderflow.dto.CartonDto;
import com.orderflow.entity.order.Order;
import com.orderflow.entity.packing.Carton;
import com.orderflow.entity.picking.Picking;
import com.orderflow.entity.user.User;
import com.orderflow.entity.warehouse.Warehouse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;
import java.util.UUID;

@Mapper(componentModel = "spring", uses = CartonItemMapper.class)
public interface CartonMapper {

    @Mapping(source = "orderId", target = "order", qualifiedByName = "uuidToOrder")
    @Mapping(source = "packerId", target = "packer", qualifiedByName = "uuidToUser")
    @Mapping(source = "warehouseId", target = "warehouse", qualifiedByName = "uuidToWarehouse")
    @Mapping(source = "pickingId", target = "picking", qualifiedByName = "uuidToPicking")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Carton cartonDtoToCarton(CartonDto cartonDto);

    @Mapping(target = "orderId", source= "order.orderId")
    @Mapping(target = "packerId", source = "packer.userId")
    @Mapping(target = "warehouseId", source = "warehouse.warehouseId")
    @Mapping(target = "pickingId", source = "picking.pickingId")
    CartonDto cartonToCartonDto(Carton carton);

    List<CartonDto> cartonsToCartonDtos(List<Carton> cartons);

    @Named("uuidToOrder")
    default Order uuidToOrder(UUID orderId) {
        if (orderId == null) return null;
        Order order = new Order();
        order.setOrderId(orderId);
        return order;
    }

    @Named("uuidToUser")
    default User uuidToUser(UUID userId) {
        if (userId == null) return null;
        User user = new User();
        user.setUserId(userId);
        return user;
    }

    @Named("uuidToWarehouse")
    default Warehouse uuidToWarehouse(UUID warehouseId) {
        if (warehouseId == null) return null;
        Warehouse warehouse = new Warehouse();
        warehouse.setWarehouseId(warehouseId);
        return warehouse;
    }

    @Named("uuidToPicking")
    default Picking uuidToPicking(UUID pickingId) {
        if (pickingId == null) return null;
        Picking picking = new Picking();
        picking.setPickingId(pickingId);
        return picking;
    }

}
