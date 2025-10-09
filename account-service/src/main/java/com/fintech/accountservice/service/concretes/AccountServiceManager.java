package com.fintech.accountservice.service.concretes;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.fintech.accountservice.dto.request.RequestCreateAccountDto;
import com.fintech.accountservice.dto.response.ResponseCreateAccountDto;
import com.fintech.accountservice.model.Account;
import com.fintech.accountservice.model.AccountStatus;
import com.fintech.accountservice.model.AccountType;
import com.fintech.accountservice.model.Currency;
import com.fintech.accountservice.repository.AccountRepository;
import com.fintech.accountservice.service.abstracts.AccountService;


@Service
public class AccountServiceManager implements AccountService {

    private final AccountRepository accountRepository;
    private static final Logger log = LoggerFactory.getLogger(AccountServiceManager.class);

    public AccountServiceManager(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public Account createDefaultAccount(UUID userId) {

        List<Account> existAccounts = accountRepository.findByUserId(userId); 
                            
        if(!existAccounts.isEmpty()){
            for (Account account : existAccounts) {
                if(account.isPrimary())
                    return account;
            }
        }

        Account account = new Account();      
        account.setUserId(userId);
        account.setAccountType(AccountType.CURRENT); // vadesiz
        account.setCurrentType(Currency.TRY);
        account.setPrimary(true);
        log.info("Kullanici için default hesap oluşturuldu! userId : {}", userId);
       
        return account;


        
    }
    public ResponseCreateAccountDto createAccount(String token,RequestCreateAccountDto requestCreateAccountDto) {

         

        return null;
    }

    


    
}
