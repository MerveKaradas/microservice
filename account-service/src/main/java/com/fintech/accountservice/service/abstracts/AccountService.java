package com.fintech.accountservice.service.abstracts;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.fintech.accountservice.dto.request.RequestCreateAccountDto;
import com.fintech.accountservice.dto.response.ResponseBalanceDto;
import com.fintech.accountservice.model.base.Account;
import com.fintech.accountservice.model.entity.OutboxEvent;
import com.fintech.common.event.account.dto.response.ResponseAccountInfoDto;

public interface AccountService {

    Account createAccount(String token,RequestCreateAccountDto requestCreateAccountDto);

    Account createDefaultAccount(UUID userId);

    Account closeAccount(String token, UUID accountId);

    List<Account> getAccounts(String token);

    Account getAccount(String token,UUID accountId);

    ResponseBalanceDto getBalance(String token,UUID accountId);

    void proccessLedgerEvent(Map<String,Object> data); 

    ResponseAccountInfoDto getAccountInfo(UUID accountId);
    
}
