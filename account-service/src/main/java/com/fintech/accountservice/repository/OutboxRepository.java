package com.fintech.accountservice.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fintech.accountservice.model.entity.OutboxEvent;

public interface OutboxRepository extends JpaRepository<OutboxEvent, UUID> {
     
    List<OutboxEvent> findTop50ByProcessedFalseOrderByCreatedAtAsc();
}
