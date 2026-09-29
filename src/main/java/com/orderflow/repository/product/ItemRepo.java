package com.orderflow.repository.product;

import com.orderflow.entity.product.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ItemRepo extends JpaRepository<Item, UUID> {
    Page<Item> findByCategory(Item.ItemCategory category, Pageable pageable);

    Page<Item> findByStatus(Item.ItemStatus status, Pageable pageable);

    Page<Item> findBySourceType(Item.InwardSource sourceType, Pageable pageable);

    @Query("SELECT i FROM Item i WHERE " + "(:search IS NULL OR LOWER(i.name) LIKE LOWER(CONCAT('%', :search, '%'))) AND " + "(:category IS NULL OR i.category = :category) AND " + "(:status IS NULL OR i.status = :status) AND " + "(:sourceType IS NULL OR i.sourceType = :sourceType)")
    Page<Item> search(@Param("search") String search, @Param("category") Item.ItemCategory category, @Param("status") Item.ItemStatus status, @Param("sourceType") Item.InwardSource sourceType, Pageable pageable);
}
