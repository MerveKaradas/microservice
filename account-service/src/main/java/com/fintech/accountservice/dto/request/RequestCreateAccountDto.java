package com.fintech.accountservice.dto.request;

import java.io.Serializable;
import java.util.UUID;

import com.fintech.accountservice.model.AccountType;
import com.fintech.accountservice.model.Currency;

public class RequestCreateAccountDto implements Serializable {

    public final static long serialVersionUID = 2001L;
    
    private UUID userId;
    private AccountType accountType;
    private Currency currency;

    
    public RequestCreateAccountDto(UUID userId, AccountType accountType, Currency currency) {
        this.userId = userId;
        this.accountType = accountType;
        this.currency = currency;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }
    public UUID getUserId() {
        return userId;
    }
    public AccountType getAccountType() {
        return accountType;
    }
    public Currency getCurrency() {
        return currency;
    }

    
    
}
