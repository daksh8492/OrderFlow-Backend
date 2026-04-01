package com.orderflow.entity.product;

import com.orderflow.entity.vendor.Vendor;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.*;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Variant {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID variantId;
    private String name;
    @Column(unique = true, nullable = false)
    private String sku;
    private BigDecimal sellingPrice;
    private BigDecimal purchasePrice;
    private Double totalQty;
//    private Double qtyAvailForSales;
//    private Double minStockLevel;
    @ElementCollection
    @CollectionTable(name = "item_variant_attributes", joinColumns = @JoinColumn(name = "variant_id"))
    @MapKeyColumn(name = "attribute_name")
    @Column(name = "attribute_value")
    private Map<String, String> attributes = new HashMap<>();
    private String barcode;
    @ElementCollection
    @CollectionTable(name = "item_variant_images", joinColumns = @JoinColumn(name = "variant_id"))
    @Column(name = "image_url")
    private List<String> imageUrls;
    private Instant createdAt;
    private Instant updatedAt;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;
    @Enumerated(EnumType.STRING)
    private VariantStatus status;
    @ManyToMany(fetch = FetchType.LAZY)
    private Set<Vendor> vendors = new HashSet<>();

    @PrePersist
    void onCreate(){
        createdAt = Instant.now();
        updatedAt = Instant.now();
    }

    @PreUpdate
    void onUpdate(){
        updatedAt = Instant.now();
    }

    public enum VariantStatus{
        ACTIVE, INACTIVE, DISCONTINUED, OUT_OF_STOCK;
    }

}
