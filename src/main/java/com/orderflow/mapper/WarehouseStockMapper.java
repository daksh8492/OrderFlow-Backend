package com.orderflow.mapper;

import com.orderflow.dto.WarehouseStockDto;
import com.orderflow.entity.product.Variant;
import com.orderflow.entity.warehouse.WarehouseLocation;
import com.orderflow.entity.warehouse.WarehouseStock;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;
import java.util.UUID;

@Mapper(componentModel = "spring")
public interface WarehouseStockMapper {

    @Mapping(source = "warehouse.warehouseId", target = "warehouseId")
    @Mapping(source = "warehouseLocation.locationId", target = "warehouseLocationId")
    @Mapping(source = "variant.variantId", target = "variantId")
    WarehouseStockDto warehouseStockToWarehouseStockDto(WarehouseStock warehouseStock);

    @Mapping(target = "warehouse", ignore = true)
    @Mapping(source = "warehouseLocationId", target = "warehouseLocation", qualifiedByName = "uuidToWarehouseLocation")
    @Mapping(source = "variantId", target = "variant", qualifiedByName = "uuidToVariant")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    WarehouseStock warehouseStockDtoToWarehouseStock(WarehouseStockDto warehouseStockDto);

    List<WarehouseStockDto> warehouseStocksToWarehouseStockDtos(List<WarehouseStock> warehouseStocks);

    @Named("uuidToWarehouseLocation")
    default WarehouseLocation uuidToWarehouseLocation(UUID locationId) {
        if (locationId == null) return null;
        WarehouseLocation location = new WarehouseLocation();
        location.setLocationId(locationId);
        return location;
    }

    @Named("uuidToVariant")
    default Variant uuidToVariant(UUID variantId) {
        if (variantId == null) return null;
        Variant variant = new Variant();
        variant.setVariantId(variantId);
        return variant;
    }
}
