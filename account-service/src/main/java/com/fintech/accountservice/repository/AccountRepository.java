package com.fintech.accountservice.repository;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import com.fintech.accountservice.model.Account;

public interface AccountRepository extends JpaRepository<Account, UUID> {
    List<Account> findByUserId(UUID userId);
    boolean existsByUserIdAndIsPrimary(UUID userId, boolean isPrimary);
    
}
