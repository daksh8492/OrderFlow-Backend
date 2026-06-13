package com.orderflow.service;

import com.orderflow.dto.CartonDto;
import com.orderflow.dto.CartonItemDto;
import com.orderflow.entity.order.Order;
import com.orderflow.entity.order.OrderItem;
import com.orderflow.entity.packing.Carton;
import com.orderflow.entity.packing.CartonItem;
import com.orderflow.entity.picking.Picking;
import com.orderflow.entity.user.User;
import com.orderflow.entity.warehouse.Warehouse;
import com.orderflow.exceptions.*;
import com.orderflow.mapper.CartonMapper;
import com.orderflow.repository.order.OrderItemRepo;
import com.orderflow.repository.order.OrderRepo;
import com.orderflow.repository.packing.CartonRepo;
import com.orderflow.repository.picking.PickingRepo;
import com.orderflow.repository.user.UserRepo;
import com.orderflow.repository.warehouse.WarehouseRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

@Service
public class CartonService {

    @Autowired
    private CartonRepo cartonRepo;

    @Autowired
    private CartonMapper cartonMapper;

    @Autowired
    private OrderRepo orderRepo;
    @Autowired
    private PickingRepo pickingRepo;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private WarehouseRepo warehouseRepo;
    @Autowired
    private OrderItemRepo orderItemRepo;

    @Transactional
    public CartonDto addCarton(CartonDto cartonDto) {

        Carton carton = cartonMapper.cartonDtoToCarton(cartonDto);

        if (carton.getCartonItems() == null) carton.setCartonItems(new HashSet<>());

        Order order = orderRepo.findById(cartonDto.getOrderId()).orElseThrow(() -> new OrderNotFoundException("Order does not exist"));

        if (order.getStatus() != Order.OrderStatus.PICKED) throw new IllegalArgumentException("Order must be PICKED before packing");

        Picking picking = pickingRepo.findById(cartonDto.getPickingId()).orElseThrow(() -> new PickingNotFoundException("Picking does not exist"));

        if (!picking.getOrder().getOrderId().equals(order.getOrderId())) throw new IllegalArgumentException("Picking does not belong to the selected order");

        User packer = userRepo.findById(cartonDto.getPackerId()).orElseThrow(() -> new UserNotFoundException("Packer does not exist"));

        Warehouse warehouse = warehouseRepo.findById(cartonDto.getWarehouseId()).orElseThrow(() -> new WarehouseNotFoundException("Warehouse does not exist"));

        if (!warehouse.getWarehouseId().equals(picking.getWarehouse().getWarehouseId())) throw new IllegalArgumentException("Warehouse does not match picking warehouse");

        carton.setOrder(order);
        carton.setPicking(picking);
        carton.setPacker(packer);
        carton.setWarehouse(warehouse);

        carton.getCartonItems().clear();

        for (CartonItemDto itemDto : cartonDto.getCartonItems()) {

            OrderItem orderItem = orderItemRepo.findById(itemDto.getOrderItemId()).orElseThrow(() -> new OrderItemNotFoundException("Order item does not exist"));

            if (!orderItem.getOrder().getOrderId().equals(order.getOrderId()))
                throw new IllegalArgumentException("Order item does not belong to the selected order");

            if (itemDto.getPackedQuantity() == null || itemDto.getPackedQuantity().compareTo(BigDecimal.ZERO) <= 0)
                throw new IllegalArgumentException("Packed quantity must be greater than zero");

            BigDecimal currentPacked = orderItem.getPackedQuantity() == null ? BigDecimal.ZERO : orderItem.getPackedQuantity();

            BigDecimal newPackedQuantity = currentPacked.add(itemDto.getPackedQuantity());

            if (newPackedQuantity.compareTo(orderItem.getQuantity()) > 0)
                throw new IllegalArgumentException("Packed quantity exceeds ordered quantity for item " + orderItem.getOrderItemId());

            orderItem.setPackedQuantity(newPackedQuantity);
            orderItemRepo.save(orderItem);

            CartonItem cartonItem = CartonItem.builder().orderItem(orderItem).packedQuantity(itemDto.getPackedQuantity()).build();

            carton.addItem(cartonItem);
        }

        carton.setStatus(Carton.CartonStatus.PACKED);
        carton.setCartonNumber(generateCartonNumber());
        Carton savedCarton = cartonRepo.save(carton);
        updateOrderPackingStatus(order);
        return cartonMapper.cartonToCartonDto(savedCarton);
    }

    @Transactional
    public List<CartonDto> getAllCartons() {
        return cartonMapper.cartonsToCartonDtos(cartonRepo.findAllByOrderByCreatedAtDesc());
    }

    @Transactional
    public CartonDto getCartonById(UUID cartonId) {
        return cartonMapper.cartonToCartonDto(cartonRepo.findById(cartonId).orElseThrow(() -> new CartonNotFoundException("The carton record does not exist")));
    }

    @Transactional
    public List<CartonDto> getCartonsByOrderId(UUID orderId) {
        return cartonMapper.cartonsToCartonDtos(cartonRepo.findAllByOrder_OrderId(orderId));
    }

    @Transactional
    public List<CartonDto> getCartonsByPackerId(UUID packerId){
        return cartonMapper.cartonsToCartonDtos(cartonRepo.findAllByPacker_UserId(packerId));
    }

    @Transactional
    public List<CartonDto> getCartonsByWarehouseId(UUID warehouseId){
        return cartonMapper.cartonsToCartonDtos(cartonRepo.findALlByWarehouse_WarehouseId(warehouseId));
    }

    @Transactional
    public List<CartonDto> getCartonsByPickingId(UUID pickingId){
        return cartonMapper.cartonsToCartonDtos(cartonRepo.findAllByPicking_PickingId(pickingId));
    }

    @Transactional
    public CartonDto updateCarton(UUID cartonId, CartonDto cartonDto) {

        Carton carton = cartonRepo.findById(cartonId).orElseThrow(() -> new CartonNotFoundException("Carton does not exist"));

        Order order = orderRepo.findById(cartonDto.getOrderId()).orElseThrow(() -> new OrderNotFoundException("Order does not exist"));

        Picking picking = pickingRepo.findById(cartonDto.getPickingId()).orElseThrow(() -> new PickingNotFoundException("Picking does not exist"));

        User packer = userRepo.findById(cartonDto.getPackerId()).orElseThrow(() -> new UserNotFoundException("Packer does not exist"));

        Warehouse warehouse = warehouseRepo.findById(cartonDto.getWarehouseId()).orElseThrow(() -> new WarehouseNotFoundException("Warehouse does not exist"));

        if (!picking.getOrder().getOrderId().equals(order.getOrderId()))
            throw new IllegalArgumentException("Picking does not belong to order");

        if (!warehouse.getWarehouseId().equals(picking.getWarehouse().getWarehouseId()))
            throw new IllegalArgumentException("Warehouse does not match picking warehouse");

        for (CartonItem oldItem : carton.getCartonItems()) {

            OrderItem orderItem = oldItem.getOrderItem();

            BigDecimal packedQuantity = orderItem.getPackedQuantity() == null ? BigDecimal.ZERO : orderItem.getPackedQuantity();

            orderItem.setPackedQuantity(packedQuantity.subtract(oldItem.getPackedQuantity()));

            orderItemRepo.save(orderItem);
        }

        carton.setOrder(order);
        carton.setPicking(picking);
        carton.setPacker(packer);
        carton.setWarehouse(warehouse);

        carton.getCartonItems().clear();

        for (CartonItemDto itemDto : cartonDto.getCartonItems()) {

            OrderItem orderItem = orderItemRepo.findById(itemDto.getOrderItemId()).orElseThrow(() -> new OrderItemNotFoundException("Order item does not exist"));

            if (!orderItem.getOrder().getOrderId().equals(order.getOrderId()))
                throw new IllegalArgumentException("Order item does not belong to the selected order");

            if (itemDto.getPackedQuantity() == null || itemDto.getPackedQuantity().compareTo(BigDecimal.ZERO) <= 0)
                throw new IllegalArgumentException("Packed quantity must be greater than zero");

            BigDecimal currentPacked = orderItem.getPackedQuantity() == null ? BigDecimal.ZERO : orderItem.getPackedQuantity();

            BigDecimal newPackedQuantity = currentPacked.add(itemDto.getPackedQuantity());

            if (newPackedQuantity.compareTo(orderItem.getQuantity()) > 0)
                throw new IllegalArgumentException("Packed quantity exceeds ordered quantity for item " + orderItem.getOrderItemId());

            orderItem.setPackedQuantity(newPackedQuantity);
            orderItemRepo.save(orderItem);

            CartonItem cartonItem = CartonItem.builder().orderItem(orderItem).packedQuantity(itemDto.getPackedQuantity()).build();

            carton.addItem(cartonItem);
        }

        updateOrderPackingStatus(order);

        Carton savedCarton = cartonRepo.save(carton);

        return cartonMapper.cartonToCartonDto(savedCarton);
    }

    @Transactional
    public void deleteCarton(UUID cartonId) {

        Carton carton = cartonRepo.findById(cartonId).orElseThrow(() -> new CartonNotFoundException("Carton does not exist"));

        Order order = carton.getOrder();

        for (CartonItem cartonItem : carton.getCartonItems()) {

            OrderItem orderItem = cartonItem.getOrderItem();

            BigDecimal packedQuantity = orderItem.getPackedQuantity() == null ? BigDecimal.ZERO : orderItem.getPackedQuantity();

            orderItem.setPackedQuantity(packedQuantity.subtract(cartonItem.getPackedQuantity()));

            orderItemRepo.save(orderItem);
        }

        cartonRepo.delete(carton);

        updateOrderPackingStatus(order);

        orderRepo.save(order);
    }

    private String generateCartonNumber() {
        Carton lastCarton = cartonRepo.findTopByOrderByCartonNumberDesc();
        if (lastCarton == null) return "CTN-0001";
        String lastNumber = lastCarton.getCartonNumber();
        int nextNumber = Integer.parseInt(lastNumber.substring(4)) + 1;

        return String.format("CTN-%04d", nextNumber);
    }

    private void updateOrderPackingStatus(Order order){
        boolean fullyPacked = true;

        for (OrderItem item : order.getItems()) {
            BigDecimal packed = item.getPackedQuantity() == null ? BigDecimal.ZERO : item.getPackedQuantity();

            if (packed.compareTo(item.getQuantity()) != 0) {
                fullyPacked = false;
                break;
            }
        }

        if (fullyPacked) order.setStatus(Order.OrderStatus.PACKED);
        else order.setStatus(Order.OrderStatus.PICKED);

        orderRepo.save(order);
    }

}
