package com.orderflow.repository.product;

import com.orderflow.entity.product.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ItemRepo extends JpaRepository<Item, UUID> {
    List<Item> findByCategory(Item.ItemCategory category);

    List<Item> findByStatus(Item.ItemStatus status);

    List<Item> findBySourceType(Item.InwardSource sourceType);
}
