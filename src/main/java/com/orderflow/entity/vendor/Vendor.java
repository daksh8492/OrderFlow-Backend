package com.orderflow.entity.vendor;

import com.orderflow.entity.product.Variant;
import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Vendor {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID vendorId;
    @Column(nullable = false, unique = true)
    private String vendorCode;
    private String vendorName;
    private String address;
    @Column(unique = true)
    private String TRN;
    private String Currency;
    private String city;
    private String vendorBrand;
    private String contactNumber;
    private String contactTelephone;
    private String contactEmail;
    private String paymentTerms;
    private Double minimumOrderValue;
    @Enumerated(EnumType.STRING)
    private VendorStatus status;
    private Instant createdAt;
    private Instant updatedAt;
    @ManyToMany(mappedBy = "vendors", fetch = FetchType.LAZY)
    private Set<Variant> variants = new HashSet<>();

    @PrePersist
    void onCreate(){
        createdAt = Instant.now();
        updatedAt = Instant.now();
    }

    @PreUpdate
    void onUpdate(){
        updatedAt = Instant.now();
    }

    public enum VendorStatus{
        INACTIVE, ACTIVE, CLOSED
    }
}
