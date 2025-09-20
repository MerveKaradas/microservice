package com.fintech.authservice.repository;
 
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.fintech.authservice.model.User;

public interface UserRepository extends JpaRepository <User, Long>{
    Optional<User> findByIdAndDeletedAtIsNull(Long id);
    boolean existsByEmail(String email);
    boolean existsByPhoneNumber(String phoneNumber);
    Optional<User> findByEmailAndDeletedAtIsNull(String email);

}
