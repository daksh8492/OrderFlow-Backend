package com.orderflow.mapper;

import com.orderflow.dto.ShipmentDto;
import com.orderflow.entity.packing.Carton;
import com.orderflow.entity.shipment.Shipment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface ShipmentMapper {

    @Mapping(target = "warehouse", ignore = true)
    @Mapping(target = "shipper", ignore = true)
    Shipment shipmentDtoToShipment(ShipmentDto shipmentDto);

    @Mapping(target = "warehouseId", source = "warehouse.warehouseId")
    @Mapping(target = "shipperId", source = "shipper.userId")
    @Mapping(target = "cartonIds", source = "cartons", qualifiedByName = "cartonsToIds")
    ShipmentDto shipmentToShipmentDto(Shipment shipment);

    List<ShipmentDto> shipmentsToShipmentDtos(List<Shipment> shipments);

    @Named("cartonsToIds")
    default Set<UUID> cartonsToIds(Set<Carton> cartons) {

        if (cartons == null) return new HashSet<>();
        return cartons.stream().map(Carton::getCartonId).collect(Collectors.toSet());
    }

}
