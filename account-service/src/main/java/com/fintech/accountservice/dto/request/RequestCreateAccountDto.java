package com.fintech.accountservice.dto.request;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

public class RequestCreateAccountDto implements Serializable {

     
    public final static long serialVersionUID = 2001L;

    //INFO : NotBlank CharSequence türleri için geçerli direkt enum üzerinden uygulanamıyor

    @NotBlank(message = "Hesap türü boş olamaz!")
    @Pattern(regexp = "CURRENT|SAVINGS|INVESTMENT|CREDIT|FOREIGN_CURRENCY")
    private String accountType;

    @NotBlank(message = "Para birimi boş olamaz!")
    @Pattern(regexp = "TRY|USD|EUR")
    private String currency;
   
    public RequestCreateAccountDto() {
    }


    public RequestCreateAccountDto(String accountType,String currency) {
        this.accountType = accountType;
        this.currency = currency;
    }


    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    

    

    
    
}
