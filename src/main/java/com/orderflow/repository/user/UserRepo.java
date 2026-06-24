package com.orderflow.repository.user;

import com.orderflow.entity.user.FieldOfWork;
import com.orderflow.entity.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Repository
public interface UserRepo extends JpaRepository<User, UUID> {
    Optional<User> findByCode(String code);

    Page<User> findByFieldOfWork(FieldOfWork fieldOfWork, Pageable pageable);

    boolean existsByCode(String code);
}
