package com.orderflow.dto;

import com.orderflow.entity.product.Variant;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VariantDto {

    private UUID variantId;
    private String name;
    private String sku;
    private BigDecimal sellingPrice;
    private BigDecimal purchasePrice;
    private Double totalQty;
    private Double qtyAvailForSales;
//    private Double minStockLevel;
    private HashMap<String, String> attributes;
    private String barcode;
    private List<String> imageUrls;
    private UUID itemId;
    private Set<UUID> vendorIds = new HashSet<>();
    private Variant.VariantStatus status;
    private Instant createdAt;
}
