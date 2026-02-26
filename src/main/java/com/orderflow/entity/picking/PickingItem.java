package com.orderflow.entity.picking;

import com.orderflow.entity.warehouse.WarehouseStock;
import com.orderflow.entity.order.OrderItem;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PickingItem {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID pickingItemId;
    @ManyToOne(fetch = FetchType.LAZY)
    private Picking picking;
//    @ManyToOne(fetch = FetchType.LAZY)
//    private Variant variant;
    @ManyToOne(fetch = FetchType.LAZY)
    private OrderItem orderItem;
    private Double pickedItems;
    @ManyToOne(fetch = FetchType.LAZY)
    private WarehouseStock pickedFrom;

}
