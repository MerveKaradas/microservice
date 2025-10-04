package com.fintech.authservice.repository;
 
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import com.fintech.authservice.model.User;

public interface UserRepository extends JpaRepository <User, Long>{
    Optional<User> findByIdAndDeletedAtIsNull(UUID id);
    boolean existsByEmail(String email);
    Optional<User> findByEmailAndDeletedAtIsNull(String email);

}
