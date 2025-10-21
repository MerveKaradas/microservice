package com.fintech.ledgerservice.repository; 

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fintech.ledgerservice.model.LedgerEntry;

public interface LedgerRepository extends JpaRepository<LedgerEntry, UUID> {
    
}
