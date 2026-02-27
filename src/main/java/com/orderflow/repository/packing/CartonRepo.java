package com.orderflow.repository.packing;

import com.orderflow.entity.packing.Carton;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CartonRepo extends JpaRepository<Carton, UUID> {
}
