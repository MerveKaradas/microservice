package com.fintech.accountservice.dto.response;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import com.fintech.accountservice.model.AccountType;
import com.fintech.accountservice.model.Currency;

public class ResponseCreateAccountDto implements Serializable{

    private final static long serialVersionUID = 1001L;

    private UUID id;
    private UUID userId;
    private String accountNumber;
    private AccountType accountType;
    private Currency currency;
    private BigDecimal balance;
    private BigDecimal availableBalance;
    private Instant createdAt;

    public ResponseCreateAccountDto(UUID id, UUID userId, String accountNumber, AccountType accountType,
            Currency currency, BigDecimal balance, BigDecimal availableBalance, Instant createdAt) {
        this.id = id;
        this.userId = userId;
        this.accountNumber = accountNumber;
        this.accountType = accountType;
        this.currency = currency;
        this.balance = balance;
        this.availableBalance = availableBalance;
        this.createdAt = createdAt;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public UUID getId() {
        return id;
    }
    public UUID getUserId() {
        return userId;
    }
    public String getAccountNumber() {
        return accountNumber;
    }
    public AccountType getAccountType() {
        return accountType;
    }
    public Currency getCurrency() {
        return currency;
    }
    public BigDecimal getBalance() {
        return balance;
    }
    public Instant getCreatedAt() {
        return createdAt;
    } 
    
    public BigDecimal getAvailableBalance() {
        return availableBalance;
    }

    
    


    
}
