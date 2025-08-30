package com.rhythmo.rhythmobackend.repository;

import com.rhythmo.rhythmobackend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    Optional<User> getByUsername(String username);
}
