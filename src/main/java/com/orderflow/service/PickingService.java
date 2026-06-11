package com.orderflow.service;

import com.orderflow.dto.PickingDto;
import com.orderflow.dto.PickingItemDto;
import com.orderflow.dto.PickingSummaryDto;
import com.orderflow.entity.order.Order;
import com.orderflow.entity.order.OrderItem;
import com.orderflow.entity.picking.Picking;
import com.orderflow.entity.picking.PickingItem;
import com.orderflow.entity.warehouse.Warehouse;
import com.orderflow.entity.warehouse.WarehouseStock;
import com.orderflow.exceptions.*;
import com.orderflow.mapper.PickingMapper;
import com.orderflow.repository.order.OrderItemRepo;
import com.orderflow.repository.order.OrderRepo;
import com.orderflow.repository.picking.PickingRepo;
import com.orderflow.repository.user.UserRepo;
import com.orderflow.repository.warehouse.WarehouseRepo;
import com.orderflow.repository.warehouse.WarehouseStockRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

@Service
public class PickingService {

    @Autowired
    private PickingRepo pickingRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private OrderRepo orderRepo;

    @Autowired
    private WarehouseRepo warehouseRepo;

    @Autowired
    private PickingMapper pickingMapper;

    @Autowired
    private OrderItemRepo orderItemRepo;

    @Autowired
    private WarehouseStockRepo warehouseStockRepo;

    @Autowired
    private WarehouseStockService warehouseStockService;

//    @Transactional
//    public PickingDto addPicking(PickingDto pickingDto) {
//        Picking picking = pickingMapper.pickingDtoToPicking(pickingDto);
//
//        if (picking.getPickingItems() == null) {
//            picking.setPickingItems(new HashSet<>());
//        }
//
//        picking.setPicker(userRepo.findById(pickingDto.getPickerId()).orElseThrow(() -> new UserNotFoundException("The picker does not exist")));
//
//        picking.setOrder(orderRepo.findById(pickingDto.getOrderId()).orElseThrow(() -> new OrderNotFoundException("The order does not exist")));
//
//        picking.setWarehouse(warehouseRepo.findById(pickingDto.getWarehouseId()).orElseThrow(() -> new WarehouseNotFoundException("The warehouse does not exist")));
//
//        Set<PickingItem> items = pickingItemMapper.pickingItemDtosToPickingItems(pickingDto.getPickingItems());
//
//        for (PickingItem item : items) {
//            item.setPicking(picking);
//            item.setOrderItem(orderItemRepo.findById(item.getOrderItem().getOrderItemId()).orElseThrow(() -> new OrderItemNotFoundException("The orderItem does not exist")));
//            item.setPickedFrom(warehouseStockRepo.findById(item.getPickedFrom().getStockId()).orElseThrow(() -> new WarehouseStockNotFoundException("The warehouse stock does not exist")));
//            picking.addItem(item);
//        }
//
//        pickingRepo.save(picking);
//        return pickingMapper.pickingToPickingDto(picking);
//    }


    @Transactional
    public PickingDto addPicking(PickingDto pickingDto) {

        validatePickedQuantities(pickingDto);

        Picking picking = pickingMapper.pickingDtoToPicking(pickingDto);

        if (picking.getPickingItems() == null) {
            picking.setPickingItems(new HashSet<>());
        }

        picking.setPicker(userRepo.findById(pickingDto.getPickerId()).orElseThrow(() -> new UserNotFoundException("The picker does not exist")));

        Order order = orderRepo.findById(pickingDto.getOrderId()).orElseThrow(() -> new OrderNotFoundException("The order does not exist"));

        if (pickingRepo.existsByOrder_OrderId(pickingDto.getOrderId())) throw new IllegalArgumentException("A picking record already exists for this order");

        picking.setOrder(order);

        Warehouse warehouse = warehouseRepo.findById(pickingDto.getWarehouseId()).orElseThrow(() -> new WarehouseNotFoundException("The warehouse does not exist"));

        picking.setWarehouse(warehouse);

        picking.getPickingItems().clear();

        for (PickingItemDto itemDto : pickingDto.getPickingItems()) {

            OrderItem orderItem = orderItemRepo.findById(itemDto.getOrderItemId()).orElseThrow(() -> new OrderItemNotFoundException("The order item does not exist"));

            WarehouseStock warehouseStock = warehouseStockRepo.findById(itemDto.getWarehouseStockId()).orElseThrow(() -> new WarehouseStockNotFoundException("The warehouse stock does not exist"));

            if (itemDto.getPickedItems() == null || itemDto.getPickedItems().compareTo(BigDecimal.ZERO) <= 0)
                throw new IllegalArgumentException("Picked quantity must be greater than zero");

            if (!orderItem.getOrder().getOrderId().equals(order.getOrderId()))
                throw new IllegalArgumentException("Order item does not belong to the selected order");

            if (!warehouseStock.getWarehouse().getWarehouseId().equals(warehouse.getWarehouseId()))
                throw new IllegalArgumentException("Warehouse stock does not belong to the selected warehouse");

//            if (itemDto.getPickedItems().compareTo(warehouseStock.getTotalQuantity()) > 0) throw new IllegalArgumentException("Picked quantity exceeds available stock");

            warehouseStockService.updateStock(warehouseStock.getStockId(), itemDto.getPickedItems().negate());

            PickingItem item = PickingItem.builder().orderItem(orderItem).pickedFrom(warehouseStock).pickedItems(itemDto.getPickedItems()).build();

            picking.addItem(item);
        }

        Picking savedPicking = pickingRepo.save(picking);

        order.setStatus(Order.OrderStatus.PICKED);
        orderRepo.save(order);

        return pickingMapper.pickingToPickingDto(savedPicking);
    }

    @Transactional
    public List<PickingSummaryDto> getAllPickings() {
        return pickingMapper.pickingsToPickingSummaryDtos(pickingRepo.findAll());
    }

    @Transactional
    public PickingDto getPickingById(UUID id) {
        return pickingMapper.pickingToPickingDto(pickingRepo.findById(id).orElseThrow(() -> new PickingNotFoundException("The Picking record does not exist")));
    }

    @Transactional
    public PickingDto getPickingByOrder(UUID orderId) {
        return pickingMapper.pickingToPickingDto(pickingRepo.findByOrder_OrderId(orderId));
    }

    @Transactional
    public List<PickingSummaryDto> getPickingByPicker(UUID pickerId) {
        return pickingMapper.pickingsToPickingSummaryDtos(pickingRepo.findByPicker_UserId(pickerId));
    }

    @Transactional
    public List<PickingSummaryDto> getPickingByWarehouse(UUID warehouseId) {
        return pickingMapper.pickingsToPickingSummaryDtos(pickingRepo.findByWarehouse_WarehouseId((warehouseId)));
    }

    @Transactional
    public PickingDto updatePicking(UUID pickingId, PickingDto pickingDto) {

        validatePickedQuantities(pickingDto);

        Picking picking = pickingRepo.findById(pickingId).orElseThrow(() -> new PickingNotFoundException("Picking not found"));

        picking.setPicker(userRepo.findById(pickingDto.getPickerId()).orElseThrow(() -> new UserNotFoundException("Picker not found")));
        picking.setOrder(orderRepo.findById(pickingDto.getOrderId()).orElseThrow(() -> new OrderNotFoundException("Order not found")));
        if (pickingRepo.existsByOrder_OrderId(pickingDto.getOrderId())) throw new IllegalArgumentException("A picking record already exists for this order");
        picking.setWarehouse(warehouseRepo.findById(pickingDto.getWarehouseId()).orElseThrow(() -> new WarehouseNotFoundException("Warehouse not found")));

        for (PickingItem oldItem : picking.getPickingItems())
            warehouseStockService.updateStock(oldItem.getPickedFrom().getStockId(), oldItem.getPickedItems());

        picking.getPickingItems().clear();

        for (PickingItemDto itemDto : pickingDto.getPickingItems()) {

            OrderItem orderItem = orderItemRepo.findById(itemDto.getOrderItemId()).orElseThrow(() -> new OrderItemNotFoundException("Order item not found"));

            WarehouseStock warehouseStock = warehouseStockRepo.findById(itemDto.getWarehouseStockId()).orElseThrow(() -> new WarehouseStockNotFoundException("Warehouse stock not found"));

            if (itemDto.getPickedItems() == null || itemDto.getPickedItems().compareTo(BigDecimal.ZERO) <= 0)
                throw new IllegalArgumentException("Picked quantity must be greater than zero");

            if (!orderItem.getOrder().getOrderId().equals(picking.getOrder().getOrderId()))
                throw new IllegalArgumentException("Order item does not belong to order");

            if (!warehouseStock.getWarehouse().getWarehouseId().equals(picking.getWarehouse().getWarehouseId()))
                throw new IllegalArgumentException("Warehouse stock does not belong to warehouse");

            warehouseStockService.updateStock(warehouseStock.getStockId(), itemDto.getPickedItems().negate());

            PickingItem item = PickingItem.builder().orderItem(orderItem).pickedFrom(warehouseStock).pickedItems(itemDto.getPickedItems()).build();

            picking.addItem(item);
        }

        Picking savedPicking = pickingRepo.save(picking);

        return pickingMapper.pickingToPickingDto(savedPicking);
    }

    @Transactional
    public void deletePicking(UUID id) {

        Picking picking = pickingRepo.findById(id).orElseThrow(() -> new PickingNotFoundException("Picking not found"));

        for (PickingItem item : picking.getPickingItems()) {
            warehouseStockService.updateStock(item.getPickedFrom().getStockId(), item.getPickedItems());
        }

        Order order = picking.getOrder();

        order.setStatus(Order.OrderStatus.PROCESSING);

        orderRepo.save(order);

        pickingRepo.delete(picking);
    }

    private void validatePickedQuantities(PickingDto pickingDto) {

        Map<UUID, BigDecimal> pickedTotals = new HashMap<>();

        for (PickingItemDto itemDto : pickingDto.getPickingItems())
            pickedTotals.merge(itemDto.getOrderItemId(), itemDto.getPickedItems(), BigDecimal::add);

        for (UUID orderItemId : pickedTotals.keySet()) {
            OrderItem orderItem = orderItemRepo.findById(orderItemId).orElseThrow(() -> new OrderItemNotFoundException("Order item not found"));

            if (pickedTotals.get(orderItemId).compareTo(orderItem.getQuantity()) != 0) {
                throw new IllegalArgumentException("Picked quantity must equal order quantity for Order Item " + orderItemId);
            }
        }
    }

}
