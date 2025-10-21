package com.fintech.accountservice.model.base;

import java.util.UUID;

import com.fintech.accountservice.model.enums.AccountStatus;
import com.fintech.accountservice.model.enums.AccountType;
import com.fintech.common.event.account.enums.Currency;

import java.math.BigDecimal;

public interface FundingSource {

    UUID getId();
    BigDecimal getAvailableBalance(); // mevcut bakiye ve kullanılabilir bakiye farklı şeyler biz burada kullanılabilir olanı alıyoruz 
    AccountType getAccountType(); // hesap türü
    Currency getCurrency(); // hesap para birimi
    AccountStatus getAccountStatus(); // hesap durumu 

}