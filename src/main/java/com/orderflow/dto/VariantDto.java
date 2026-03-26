package com.orderflow.dto;

import com.orderflow.entity.product.Variant;

import java.math.BigDecimal;
import java.util.*;

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
    private Variant.VariantStatus status;
}
