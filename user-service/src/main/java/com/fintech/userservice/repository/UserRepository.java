package com.fintech.userservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.fintech.userservice.model.User;

public interface UserRepository extends JpaRepository<User, String> {
    
}
