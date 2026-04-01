package com.orderflow.dto;

import com.orderflow.entity.product.Item;
import lombok.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ItemDto {

    private UUID itemId;
    private String name;
    private Item.InwardSource sourceType;
    private Item.ItemCategory category;
    private Item.ItemStatus status;
    private Instant createdAt;
    private List<VariantDto> variants = new ArrayList<>();
}
