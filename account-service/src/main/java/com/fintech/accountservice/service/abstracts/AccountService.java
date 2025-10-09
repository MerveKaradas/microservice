package com.fintech.accountservice.service.abstracts;

import java.util.UUID;

import com.fintech.accountservice.dto.request.RequestCreateAccountDto;
import com.fintech.accountservice.dto.response.ResponseCreateAccountDto;
import com.fintech.accountservice.model.Account;

public interface AccountService {

    Account createAccount(String token,RequestCreateAccountDto requestCreateAccountDto);

    Account createDefaultAccount(UUID userId);
    
}
