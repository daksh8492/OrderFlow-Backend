package com.orderflow.dto;

import com.orderflow.entity.product.Item;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ItemDto {

    private UUID itemId;
    private String name;
    private Item.InwardSource sourceType;
    private Item.ItemCategory category;
    private Item.ItemStatus status;
    private List<UUID> variantIds = new ArrayList<>();
}
