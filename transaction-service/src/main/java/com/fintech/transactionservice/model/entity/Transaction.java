package com.fintech.transactionservice.model.entity;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.fintech.common.event.account.enums.Currency;
import com.fintech.common.event.transaction.enums.TransactionType;
import com.fintech.transactionservice.model.enums.TransactionStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Entity
@Table(name = "transactions")
@AllArgsConstructor
@Builder
@Data
public class Transaction {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
        name = "UUID",
        strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name= "id", updatable = false, nullable = false, columnDefinition = "uuid" )
    private UUID id;

    @Column(nullable = false)
    private TransactionStatus transactionStatus;

    @Column(nullable = false)
    private TransactionType transactionType;

    @Column(nullable = false)
    private Currency currency;
    
    @Column(nullable = false)
    private BigDecimal amount;

    @Builder.Default
    @Column(nullable = false, updatable = false)
    private Instant createdAt = Instant.now();

    @Builder.Default
    private Instant updateAt = null;

    private String description;

    @Lob
    private String failedReason;

    public Instant setUpdateAt(){
        this.updateAt = Instant.now();
        return updateAt;
    }

    

    
    
}
