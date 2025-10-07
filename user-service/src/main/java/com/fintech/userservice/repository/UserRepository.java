package com.fintech.userservice.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import com.fintech.userservice.model.User;

public interface UserRepository extends JpaRepository<User, String> {
    
    Optional<User> findByIdAndDeletedAtIsNull(UUID id);

}
