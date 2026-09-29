package com.orderflow.repository.customer;

import com.orderflow.entity.customer.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CustomerRepo extends JpaRepository<Customer, UUID> {
    boolean existsByCustomerCode(String customerCode);

    Optional<Customer> findByCustomerCode(String customerCode);

    @Query("""
            SELECT c
            FROM Customer c
            WHERE (:status IS NULL OR c.status = :status)
              AND (
                    :search IS NULL
                 OR :search = ''
                 OR LOWER(c.customerName) LIKE LOWER(CONCAT('%', :search, '%'))
                 OR LOWER(c.contactEmail) LIKE LOWER(CONCAT('%', :search, '%'))
                 OR LOWER(c.customerCode) LIKE LOWER(CONCAT('%', :search, '%'))
              )
            """)
    Page<Customer> searchCustomers(@Param("search") String search, @Param("status") Customer.CustomerStatus status, Pageable pageable);
}
