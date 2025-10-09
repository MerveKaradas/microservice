package com.fintech.accountservice.service.concretes;

import java.util.List;
import java.util.Random;
import java.util.UUID;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fintech.accountservice.dto.request.RequestCreateAccountDto;
import com.fintech.accountservice.event.AccountCreatedEvent;
import com.fintech.accountservice.model.Account;
import com.fintech.accountservice.model.AccountType;
import com.fintech.accountservice.model.Currency;
import com.fintech.accountservice.model.OutboxEvent;
import com.fintech.accountservice.repository.AccountRepository;
import com.fintech.accountservice.repository.OutboxRepository;
import com.fintech.accountservice.repository.UserFlagsRepository;
import com.fintech.accountservice.security.JwtUtil;
import com.fintech.accountservice.service.abstracts.AccountService;


@Service
public class AccountServiceManager implements AccountService {

    private final AccountRepository accountRepository;
    private final UserFlagsRepository userFlagsRepository;
    private final OutboxRepository outboxRepository;
    private final JwtUtil jwtUtil;
    private static final Logger log = LoggerFactory.getLogger(AccountServiceManager.class);

    public AccountServiceManager(AccountRepository accountRepository, 
                                UserFlagsRepository userFlagsRepository, 
                                JwtUtil jwtUtil, 
                                OutboxRepository outboxRepository) {
        this.accountRepository = accountRepository;
        this.userFlagsRepository = userFlagsRepository;
        this.outboxRepository = outboxRepository;
        this.jwtUtil = jwtUtil;
    }

    @Transactional
    public Account createDefaultAccount(UUID userId) {

        List<Account> existAccounts = accountRepository.findByUserId(userId); 
                            
        if(!existAccounts.isEmpty()){

            for (Account account : existAccounts) {
                if(account.isPrimary())
                    return account;
            }
        }

        String accountNumber;
        do {
            accountNumber = generateAccountNumber(userId);
        } while(accountRepository.existsByAccountNumber(accountNumber));

        Account account = new Account();      
        account.setUserId(userId);
        account.setAccountType(AccountType.CURRENT); // vadesiz
        account.setCurrency(Currency.TRY);
        account.setPrimary(true);
        account.setAccountNumber(accountNumber);
        log.info("Kullanici için default hesap oluşturuldu! userId : {}", userId);

        accountRepository.save(account);
       
        return account;
        
    }

    @Transactional()
    public Account createAccount(String token,RequestCreateAccountDto request) {

        UUID userId = UUID.fromString(jwtUtil.extractUsername(token));
        if (!userFlagsRepository.existsByUserIdAndProfileCompleteTrue(userId)) {
            throw new IllegalStateException("Kullanıcı profili tamamlanmadan hesap açılamaz.");
        }

        String accountNumber;
        do {
            accountNumber = generateAccountNumber(userId);
        } while(accountRepository.existsByAccountNumber(accountNumber));

        Account account = Account.builder()
        .userId(request.getUserId())
        .currency(request.getCurrency() )
        .accountType(request.getAccountType())
        .accountNumber(accountNumber)
        .build();
        
        accountRepository.save(account);

        OutboxEvent outboxEvent = new OutboxEvent();
        outboxEvent.setTopic("account-created");
        ObjectMapper mapper = new ObjectMapper();
        try {
            outboxEvent.setPayload(mapper.writeValueAsString(
                    new AccountCreatedEvent(account.getId().toString(), account.getUserId().toString())
            ));
        } catch (JsonProcessingException e) {
            log.warn("Hata : {} " , e.getMessage());
            e.printStackTrace();
        }
        outboxRepository.save(outboxEvent);

        return account;
    }

    public static String generateAccountNumber(UUID userId) {
        String userPart = userId.toString().replaceAll("-", "").substring(0, 6); // 6 hane
        long timestamp = System.currentTimeMillis() % 1000000000; // 9 hane
        Random random = new Random();
        int randDigit = random.nextInt(10); // 1 hane
        return userPart + timestamp + randDigit;
    }




    


    
}
