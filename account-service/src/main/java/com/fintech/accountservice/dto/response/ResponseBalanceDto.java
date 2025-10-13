package com.fintech.accountservice.dto.response;

import java.io.Serializable;
import java.math.BigDecimal;

public class ResponseBalanceDto implements Serializable {

    private static final long serialVersionUID = 1002L;

    private BigDecimal balance;
    private BigDecimal availableBalance;

    public ResponseBalanceDto(BigDecimal balance, BigDecimal availableBalance) {
        this.balance = balance;
        this.availableBalance = availableBalance;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public BigDecimal getBalance() {
        return balance;
    }
   
    public BigDecimal getAvailableBalance() {
        return availableBalance;
    }
  
}
