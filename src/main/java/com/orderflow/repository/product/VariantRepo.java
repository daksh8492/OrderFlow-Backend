package com.orderflow.repository.product;

import com.orderflow.entity.product.Variant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface VariantRepo extends JpaRepository<Variant, UUID> {

    Optional<Variant> findByBarcode(String barcode);

    List<Variant> findByItem_ItemId(UUID itemItemId);

    boolean existsBySku(String sku);

    boolean existsByBarcode(String barcode);

    Optional<Variant> findBySkuIgnoreCase(String sku);
}
