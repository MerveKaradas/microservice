package com.fintech.ledgerservice.model;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.fintech.common.event.account.enums.Currency;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
@Table(name = "ledger_entries")
@Entity
public class LedgerEntry {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
        name = "UUID",
        strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id",nullable = false, updatable = false, columnDefinition = "uuid")
    private UUID id;

    @Column(nullable = false)
    private UUID transactionId;

    @Column(nullable = false)
    private UUID accountId;

    private Currency currency;

    @Column(nullable = false)
    @Builder.Default
    private BigDecimal debit = BigDecimal.ZERO; // verici


    @Column(nullable = false)
    @Builder.Default
    private BigDecimal credit = BigDecimal.ZERO; // alıcı

    @Builder.Default
    private Instant createdAt = Instant.now();
    
}
