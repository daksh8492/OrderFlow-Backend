package com.orderflow.service;

import com.orderflow.dto.ShipmentDto;
import com.orderflow.entity.order.Order;
import com.orderflow.entity.packing.Carton;
import com.orderflow.entity.shipment.Shipment;
import com.orderflow.exceptions.CartonNotFoundException;
import com.orderflow.exceptions.ShipmentNotFoundException;
import com.orderflow.mapper.ShipmentMapper;
import com.orderflow.repository.order.OrderRepo;
import com.orderflow.repository.packing.CartonRepo;
import com.orderflow.repository.shipment.ShipmentRepo;
import com.orderflow.util.AuthUtil;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ShipmentService {

    @Autowired
    private ShipmentMapper shipmentMapper;

    @Autowired
    private AuthUtil authUtil;

    @Autowired
    private ShipmentRepo shipmentRepo;

    @Autowired
    private CartonRepo cartonRepo;

    @Autowired
    private OrderRepo orderRepo;


    @Transactional
    public ShipmentDto addShipment() {

        Shipment shipment = new Shipment();

        shipment.setShipper(authUtil.getLoggedInUser());
        shipment.setWarehouse(authUtil.getLoggedInUserWarehouse());
        shipment.setShipmentNumber(generateShipmentNumber());
        shipment.setStatus(Shipment.Status.DOCKING);

        if (shipment.getCartons() == null) shipment.setCartons(new HashSet<>());

        Shipment savedShipment = shipmentRepo.save(shipment);

        return shipmentMapper.shipmentToShipmentDto(savedShipment);
    }

    @Transactional
    public List<ShipmentDto> getAllShipments() {
        return shipmentMapper.shipmentsToShipmentDtos(shipmentRepo.findAllByOrderByCreatedAtDesc());
    }

    @Transactional
    public ShipmentDto getShipmentById(UUID id) {
        return shipmentMapper.shipmentToShipmentDto(shipmentRepo.findById(id).orElseThrow(() -> new ShipmentNotFoundException("The shipment record does not exist")));
    }

    @Transactional
    public ShipmentDto addCartonToShipment(UUID shipmentId, UUID cartonId) {

        Shipment shipment = shipmentRepo.findById(shipmentId).orElseThrow(() -> new ShipmentNotFoundException("The shipment record does not exist"));
        if (shipment.getStatus() != Shipment.Status.DOCKING) throw new IllegalArgumentException("Cartons can only be added while shipment is docking");
        Carton carton = cartonRepo.findById(cartonId).orElseThrow(() -> new CartonNotFoundException("The carton record does not exist"));
        if (shipment.getCartons().contains(carton)) throw new IllegalArgumentException("Carton already exists in shipment");
        if (carton.getStatus() != Carton.CartonStatus.PACKED) throw new IllegalArgumentException("Only packed cartons can be added to shipment");

        if (!shipmentRepo.existsByCartonsContaining(carton)) throw new IllegalArgumentException("Carton already belongs to another shipment");

        shipment.addCarton(carton);

        return shipmentMapper.shipmentToShipmentDto(shipmentRepo.save(shipment));
    }

    @Transactional
    public ShipmentDto removeCartonFromShipment(UUID shipmentId, UUID cartonId) {

        Shipment shipment = shipmentRepo.findById(shipmentId).orElseThrow(() -> new ShipmentNotFoundException("The shipment record does not exist"));
        if (shipment.getStatus() != Shipment.Status.DOCKING) throw new IllegalArgumentException("Cartons can only be removed while shipment is docking");
        Carton carton = cartonRepo.findById(cartonId).orElseThrow(() -> new CartonNotFoundException("The carton record does not exist"));
        if (!shipment.getCartons().contains(carton)) throw new IllegalArgumentException("Carton does not belong to shipment");
        shipment.removeCarton(carton);

        return shipmentMapper.shipmentToShipmentDto(shipmentRepo.save(shipment));
    }

    @Transactional
    public ShipmentDto dispatchShipment(UUID shipmentId) {

        Shipment shipment = shipmentRepo.findById(shipmentId).orElseThrow(() -> new ShipmentNotFoundException("The shipment record does not exist"));
        if (shipment.getStatus() != Shipment.Status.DOCKING) throw new IllegalArgumentException("Shipment is already dispatched");
        if (shipment.getCartons().isEmpty()) throw new IllegalArgumentException("Shipment must contain at least one carton");

        Set<Order> orders = shipment.getCartons()
                .stream()
                .map(Carton::getOrder)
                .collect(Collectors.toSet());

        for (Order order : orders) {
            long shipmentCartons = shipment.getCartons()
                    .stream()
                    .filter(carton -> carton.getOrder().getOrderId().equals(order.getOrderId()))
                    .count();
            long orderCartons = cartonRepo.findAllByOrder_OrderId(order.getOrderId()).size();
            if (shipmentCartons != orderCartons) throw new IllegalArgumentException("All cartons of order " + order.getOrderNumber() + " must be added before dispatch");
        }

        shipment.setStatus(Shipment.Status.IN_TRANSIT);

        for (Carton carton : shipment.getCartons()) {
            carton.setStatus(Carton.CartonStatus.SHIPPED);
            Order order = carton.getOrder();
            if (order.getStatus() == Order.OrderStatus.PACKED) {
                order.setStatus(Order.OrderStatus.SHIPPED);
                orderRepo.save(order);
            }
            cartonRepo.save(carton);
        }
        shipment.setDispatchAt(Instant.now());

        return shipmentMapper.shipmentToShipmentDto(shipmentRepo.save(shipment));
    }

    @Transactional
    public ShipmentDto deliverShipment(UUID shipmentId, Set<UUID> cartonIds) {

        Shipment shipment = shipmentRepo.findById(shipmentId).orElseThrow(() -> new ShipmentNotFoundException("Shipment does not exist"));
        if (shipment.getStatus() != Shipment.Status.IN_TRANSIT)
            throw new IllegalArgumentException("Only in transit shipments can be delivered");
        if (cartonIds == null || cartonIds.isEmpty())
            throw new IllegalArgumentException("At least one carton must be scanned");

        Set<Carton> scannedCartons = new HashSet<>();
        Order order = null;

        for (UUID cartonId : cartonIds) {

            Carton carton = cartonRepo.findById(cartonId).orElseThrow(() -> new CartonNotFoundException("Carton does not exist"));

            if (!shipment.getCartons().contains(carton))
                throw new IllegalArgumentException("Carton does not belong to shipment");
            if (carton.getStatus() == Carton.CartonStatus.DELIVERED)
                throw new IllegalArgumentException("Carton is already delivered");
            if (order == null) order = carton.getOrder();
            if (!order.getOrderId().equals(carton.getOrder().getOrderId()))
                throw new IllegalArgumentException("All scanned cartons must belong to the same order");

            scannedCartons.add(carton);
        }

        final UUID orderId = order.getOrderId();
        Set<Carton> orderCartons = shipment.getCartons().stream().filter(carton -> carton.getOrder().getOrderId().equals(orderId)).collect(Collectors.toSet());

        if (scannedCartons.size() != orderCartons.size())
            throw new IllegalArgumentException("All cartons of the order must be scanned");
        if (!scannedCartons.containsAll(orderCartons))
            throw new IllegalArgumentException("Some cartons of the order are missing");
        if (order.getStatus() != Order.OrderStatus.SHIPPED)
            throw new IllegalArgumentException("Order is not ready for delivery");

        for (Carton carton : scannedCartons) {
            carton.setStatus(Carton.CartonStatus.DELIVERED);
            cartonRepo.save(carton);
        }

        order.setStatus(Order.OrderStatus.DELIVERED);
        orderRepo.save(order);

        boolean allShipmentCartonsDelivered = shipment.getCartons().stream().allMatch(carton -> carton.getStatus() == Carton.CartonStatus.DELIVERED);

        if (allShipmentCartonsDelivered) {
            shipment.setStatus(Shipment.Status.DELIVERED);
            shipment.setDeliveredAt(Instant.now());
            shipmentRepo.save(shipment);
        }

        return shipmentMapper.shipmentToShipmentDto(shipment);
    }

    @Transactional
    public void deleteShipment(UUID shipmentId) {

        Shipment shipment = shipmentRepo.findById(shipmentId).orElseThrow(() -> new ShipmentNotFoundException("The shipment record does not exist"));
        if (shipment.getStatus() != Shipment.Status.DOCKING) throw new IllegalArgumentException("Only docking shipments can be deleted");
        if (!shipment.getCartons().isEmpty()) throw new IllegalArgumentException("Remove all cartons from shipment before deleting it");

        shipmentRepo.delete(shipment);
    }

    private String generateShipmentNumber() {
        Shipment lastShipment = shipmentRepo.findTopByOrderByShipmentNumberDesc();
        if (lastShipment == null) return "SHP-0001";
        int next = Integer.parseInt(lastShipment.getShipmentNumber().substring(4)) + 1;
        return String.format("SHP-%04d", next);
    }
}
