package com.orderflow.repository.product;

import com.orderflow.entity.product.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ItemRepo extends JpaRepository<Item, UUID> {
    Page<Item> findByCategory(Item.ItemCategory category, Pageable pageable);

    Page<Item> findByStatus(Item.ItemStatus status, Pageable pageable);

    Page<Item> findBySourceType(Item.InwardSource sourceType, Pageable pageable);
}
