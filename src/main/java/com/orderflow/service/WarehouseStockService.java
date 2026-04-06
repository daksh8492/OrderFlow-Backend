package com.orderflow.service;

import com.orderflow.dto.WarehouseDto;
import com.orderflow.dto.WarehouseStockDto;
import com.orderflow.entity.product.Variant;
import com.orderflow.entity.warehouse.Warehouse;
import com.orderflow.entity.warehouse.WarehouseLocation;
import com.orderflow.entity.warehouse.WarehouseStock;
import com.orderflow.exceptions.VariantNotFoundException;
import com.orderflow.exceptions.WarehouseLocationNotFoundException;
import com.orderflow.exceptions.WarehouseStockNotFoundException;
import com.orderflow.mapper.WarehouseLocationMapper;
import com.orderflow.mapper.WarehouseStockMapper;
import com.orderflow.repository.product.VariantRepo;
import com.orderflow.repository.warehouse.WarehouseLocationRepo;
import com.orderflow.repository.warehouse.WarehouseStockRepo;
import com.orderflow.util.AuthUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class WarehouseStockService {

    @Autowired
    private WarehouseStockRepo warehouseStockRepo;

    @Autowired
    private WarehouseLocationRepo warehouseLocationRepo;

    @Autowired
    private WarehouseStockMapper warehouseStockMapper;

    @Autowired
    private VariantRepo variantRepo;

    @Autowired
    private AuthUtil authUtil;

    @Transactional
    public WarehouseStockDto addWarehouseStock(WarehouseStockDto warehouseStockDto) {
        WarehouseStock warehouseStock = warehouseStockMapper.warehouseStockDtoToWarehouseStock(warehouseStockDto);
        WarehouseLocation location = warehouseLocationRepo.findById(warehouseStockDto.getWarehouseLocationId()).orElseThrow(() -> new WarehouseLocationNotFoundException("Warehouse location not found"));
        warehouseStock.setWarehouseLocation(location);
        warehouseStock.setWarehouse(location.getWarehouse());
        if (warehouseStock.getTotalQuantity() == null) warehouseStock.setTotalQuantity(0.0);
        Variant variant = variantRepo.findById(warehouseStockDto.getVariantId()).orElseThrow(() -> new VariantNotFoundException("Variant does not exist"));
        if (warehouseStockRepo.existsByVariantAndWarehouseLocation(variant, location)) {
            throw new IllegalArgumentException("Warehouse Stock already exists at this location for this variant");
        }
        return warehouseStockMapper.warehouseStockToWarehouseStockDto(warehouseStockRepo.save(warehouseStock));
    }

    @Transactional
    public WarehouseStockDto updateStock(UUID warehouseStockId, Double quantity) {
        WarehouseStock warehouseStock = warehouseStockRepo.findById(warehouseStockId).orElseThrow( () -> new WarehouseStockNotFoundException("Warehouse stock not found"));
        if (quantity <= 0.0 && warehouseStock.getTotalQuantity() < Math.abs(quantity)) {
            throw new IllegalArgumentException("Total quantity is too low");
        }
        warehouseStock.setTotalQuantity(warehouseStock.getTotalQuantity() + quantity);
        return warehouseStockMapper.warehouseStockToWarehouseStockDto(warehouseStockRepo.save(warehouseStock));
    }

//    @Transactional
//    public WarehouseStockDto decreaseStock(UUID variantId, UUID locationId, Double quantity) {
//        Variant variant = variantRepo.findById(variantId).orElseThrow(() -> new VariantNotFoundException("Variant does not exist"));
//        WarehouseLocation location = warehouseLocationRepo.findById(locationId).orElseThrow(() -> new WarehouseLocationNotFoundException("Location does not exist"));
//        WarehouseStock warehouseStock = warehouseStockRepo.findByVariantAndWarehouseLocation(variant, location).orElseThrow(() -> new WarehouseStockNotFoundException("Warehouse Stock does not exist for the provided location and variant"));
//        if (warehouseStock.getTotalQuantity() < quantity) {
//            throw new IllegalArgumentException("Total quantity is too low");
//        }
//        warehouseStock.setTotalQuantity(warehouseStock.getTotalQuantity() - quantity);
//        `if (warehouseStock.getTotalQuantity() <= 0.0) {
//            warehouseStockRepo.delete(warehouseStock);
//            return warehouseStockMapper.warehouseStockToWarehouseStockDto(warehouseStock);
//        }`
//        return warehouseStockMapper.warehouseStockToWarehouseStockDto(warehouseStockRepo.save(warehouseStock));
//    }

    @Transactional
    public WarehouseStockDto transferStock(UUID sourceID, UUID targetId, Double transferQuantity) {
        WarehouseStock sourceStock = warehouseStockRepo.findById(sourceID).orElseThrow(() -> new WarehouseStockNotFoundException("Source Warehouse Stock not found"));
        if (sourceStock.getTotalQuantity() < transferQuantity)
            throw new IllegalArgumentException("Source Warehouse Stock does not have enough quantity");
        WarehouseStock targetStock = warehouseStockRepo.findById(targetId).orElseThrow(() -> new WarehouseStockNotFoundException("Target Warehouse Stock not found"));
        if (sourceStock.getVariant() != targetStock.getVariant())
            throw new IllegalArgumentException("The Variants do not match");
        if (sourceStock.getWarehouse() != targetStock.getWarehouse())
            throw new IllegalArgumentException("The Warehouses do not match");
        targetStock.setTotalQuantity(targetStock.getTotalQuantity() + transferQuantity);
        sourceStock.setTotalQuantity(sourceStock.getTotalQuantity() - transferQuantity);
        if (sourceStock.getTotalQuantity() <= 0.0) warehouseStockRepo.delete(sourceStock);
        else warehouseStockRepo.save(sourceStock);
        return warehouseStockMapper.warehouseStockToWarehouseStockDto(warehouseStockRepo.save(targetStock));
    }

    @Transactional
    public WarehouseStockDto getWarehouseStockById(UUID warehouseStockId) {
        return warehouseStockMapper.warehouseStockToWarehouseStockDto(warehouseStockRepo.findById(warehouseStockId).orElseThrow(() -> new WarehouseStockNotFoundException("Warehouse stock not found")));
    }

    @Transactional
    public List<WarehouseStockDto> getWarehouseStockByLocation(UUID locationId) {
        return warehouseStockMapper.warehouseStocksToWarehouseStockDtos(warehouseStockRepo.findByWarehouseLocation(warehouseLocationRepo.findById(locationId).orElseThrow(() -> new WarehouseLocationNotFoundException("Location does not exist"))));
    }

    @Transactional
    public List<WarehouseStockDto> getWarehouseStockByVariant(UUID variantId) {
        Warehouse warehouse = authUtil.getLoggedInUserWarehouse();
        Variant variant = variantRepo.findById(variantId).orElseThrow(() -> new VariantNotFoundException("Variant does not exist"));
        return warehouseStockMapper.warehouseStocksToWarehouseStockDtos(warehouseStockRepo.findByVariantAndWarehouse(variant, warehouse));
    }

    public void deleteWarehouseStock(UUID warehouseStockId) {
        WarehouseStock warehouseStock = warehouseStockRepo.findById(warehouseStockId).orElseThrow( () -> new WarehouseStockNotFoundException("Warehouse stock not found"));
        if (warehouseStock.getTotalQuantity() != null && warehouseStock.getTotalQuantity() != 0.0) throw new IllegalArgumentException("Total quantity is not 0");
        warehouseStockRepo.delete(warehouseStock);
    }

//    Qty avail code will be written after the order and picking apis.

}
