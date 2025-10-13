package com.fintech.accountservice.dto.request;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;

import com.fintech.accountservice.model.AccountType;
import com.fintech.accountservice.model.Currency;

public class RequestCreateAccountDto implements Serializable {

    public final static long serialVersionUID = 2001L;
    
    @NotBlank(message = "Hesap türü boş olamaz!")
    private AccountType accountType;

    @NotBlank(message = "Para birimi boş olamaz!")
    private Currency currency;
   
    public RequestCreateAccountDto() {
    }

    public RequestCreateAccountDto(AccountType accountType, Currency currency) {
        this.accountType = accountType;
        this.currency = currency;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }
    
    public AccountType getAccountType() {
        return accountType;
    }
    public Currency getCurrency() {
        return currency;
    }

    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    

    
    
}
