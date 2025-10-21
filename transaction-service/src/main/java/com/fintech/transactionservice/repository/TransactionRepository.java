package com.fintech.transactionservice.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fintech.transactionservice.model.entity.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction,UUID>{
    
}
