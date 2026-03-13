package com.orderflow.repository.vendor;

import com.orderflow.entity.vendor.Vendor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface VendorRepo extends JpaRepository<Vendor, UUID> {

    boolean existsByVendorCode(String vendorCode);

    Optional<Vendor> findByVendorCode(String vendorCode);
}
