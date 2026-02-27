package com.orderflow.repository.packing;

import com.orderflow.entity.packing.CartonItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CartonItemRepo extends JpaRepository<CartonItem, UUID> {
}
