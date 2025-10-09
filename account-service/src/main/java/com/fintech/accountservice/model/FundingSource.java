package com.fintech.accountservice.model;

import java.util.UUID;
import java.math.BigDecimal;

public interface FundingSource {

    UUID getId();
    BigDecimal getAvailableBalance(); // mevcut bakiye ve kullanılabilir bakiye farklı şeyler biz burada kullanılabilir olanı alıyoruz 
    AccountType getAccountType(); // hesap türü
    Currency getCurrency(); // hesap para birimi
    AccountStatus getAccountStatus(); // hesap durumu 

}