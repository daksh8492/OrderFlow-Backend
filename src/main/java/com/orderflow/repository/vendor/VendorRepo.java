package com.orderflow.repository.vendor;

import com.orderflow.entity.vendor.Vendor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface VendorRepo extends JpaRepository<Vendor, UUID> {

    boolean existsByVendorCode(String vendorCode);

    Optional<Vendor> findByVendorCode(String vendorCode);

    @Query("""
            SELECT v
            FROM Vendor v
            WHERE (:search IS NULL OR
                   LOWER(v.vendorName) LIKE LOWER(CONCAT('%', :search, '%')) OR
                   LOWER(v.vendorCode) LIKE LOWER(CONCAT('%', :search, '%')) OR
                   LOWER(v.city) LIKE LOWER(CONCAT('%', :search, '%')) OR
                   LOWER(v.contactEmail) LIKE LOWER(CONCAT('%', :search, '%')))
            AND (:status IS NULL OR v.status = :status)
            """)
    Page<Vendor> searchVendors(@Param("search") String search, @Param("status") Vendor.VendorStatus status, Pageable pageable);
}
