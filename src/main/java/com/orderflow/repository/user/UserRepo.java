package com.orderflow.repository.user;

import com.orderflow.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Repository
public interface UserRepo extends JpaRepository<User, UUID> {
    Optional<User> findByCode(String code);

    List<User> findByFieldOfWork(User.FieldOfWork fieldOfWork);

    boolean existsByCode(String code);
}
