package com.orderflow.repository.product;

import com.orderflow.entity.product.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ItemRepo extends JpaRepository<Item, UUID> {
}
