package com.fintech.common.event.account.dto.response;

import java.math.BigDecimal;

import com.fintech.common.event.account.enums.Currency;

public class ResponseAccountInfoDto {

    private Currency currency;
    private BigDecimal balance; //TODO : Available balance devreye girecek

    
    public ResponseAccountInfoDto() {
    }
    
    public ResponseAccountInfoDto(Currency currency, BigDecimal balance) {
        this.currency = currency;
        this.balance = balance;
    }
    public Currency getCurrency() {
        return currency;
    }
    public void setCurrency(Currency currency) {
        this.currency = currency;
    }
    public BigDecimal getBalance() {
        return balance;
    }
    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    
    
}
