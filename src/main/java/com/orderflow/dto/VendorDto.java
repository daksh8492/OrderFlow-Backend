package com.orderflow.dto;

import com.orderflow.entity.product.Variant;
import com.orderflow.entity.vendor.Vendor;
import lombok.*;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;


@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VendorDto {

    private UUID vendorId;
    private String vendorCode;
    private String vendorName;
    private String address;
    private String TRN;
    private String currency;
    private String city;
    private String vendorBrand;
    private String contactNumber;
    private String contactTelephone;
    private String contactEmail;
    private String paymentTerms;
    private Double minimumOrderValue;
    private Vendor.VendorStatus status;
    private Set<UUID> variantIds = new HashSet<>();
}
