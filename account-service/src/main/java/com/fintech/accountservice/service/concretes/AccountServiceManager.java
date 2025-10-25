package com.fintech.accountservice.service.concretes;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fintech.accountservice.dto.request.RequestCreateAccountDto;
import com.fintech.accountservice.dto.response.ResponseBalanceDto;
import com.fintech.accountservice.model.base.Account;
import com.fintech.accountservice.model.entity.OutboxEvent;
import com.fintech.accountservice.model.enums.AccountStatus;
import com.fintech.accountservice.model.enums.AccountType;
import com.fintech.accountservice.repository.AccountRepository;
import com.fintech.accountservice.repository.OutboxRepository;
import com.fintech.accountservice.repository.UserFlagsRepository;
import com.fintech.accountservice.security.JwtUtil;
import com.fintech.accountservice.service.abstracts.AccountService;
import com.fintech.common.event.account.AccountCreatedEvent;
import com.fintech.common.event.account.dto.response.ResponseAccountInfoDto;
import com.fintech.common.event.account.enums.Currency;
import com.fintech.common.event.transaction.enums.TransactionType;


@Service
public class AccountServiceManager implements AccountService {

    private final AccountRepository accountRepository;
    private final AccountFactory accountFactory;
    private final UserFlagsRepository userFlagsRepository;
    private final OutboxRepository outboxRepository;
    private final ObjectMapper objectMapper;
    private final JwtUtil jwtUtil;
    private static final Logger log = LoggerFactory.getLogger(AccountServiceManager.class);

    public AccountServiceManager(AccountRepository accountRepository, 
                                UserFlagsRepository userFlagsRepository, 
                                JwtUtil jwtUtil, 
                                OutboxRepository outboxRepository, AccountFactory accountFactory, ObjectMapper objectMapper) {
        this.accountRepository = accountRepository;
        this.accountFactory = accountFactory;
        this.userFlagsRepository = userFlagsRepository;
        this.outboxRepository = outboxRepository;
        this.objectMapper = objectMapper;
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


        Account account = accountFactory.createAccount(userId, accountNumber, AccountType.CURRENT, Currency.TRY);
        account.setPrimary(true);
        log.info("Kullanici için default hesap oluşturuldu! userId : {}", userId);

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

    @Transactional
    public Account createAccount(String token,RequestCreateAccountDto request) {

        log.info("Gelen request : accounttype " + request.getAccountType() + " currency : " + request.getCurrency());

        UUID userId = jwtUtil.extractUserId(token);
        if (!userFlagsRepository.existsByUserIdAndProfileCompleteTrue(userId)) {
            throw new IllegalStateException("Kullanıcı profili tamamlanmadan hesap açılamaz.");
        }

        String accountNumber;
        do {
            accountNumber = generateAccountNumber(userId);
        } while(accountRepository.existsByAccountNumber(accountNumber));

        Account account = accountFactory.createAccount(userId, accountNumber, AccountType.valueOf(request.getAccountType()), Currency.valueOf(request.getCurrency()));
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


    @Transactional
    public Account closeAccount(String token, UUID accountId){

        UUID userId = jwtUtil.extractUserId(token);

        if (!userFlagsRepository.existsByUserIdAndProfileCompleteTrue(userId)) {
            throw new IllegalStateException("Kullanıcı profil bilgileri tamamlanmadan önce mevcut işleme devam edilemez.");
        }

        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Mevcut bir hesap bulunamadı!"));

        if (!account.getUserId().equals(userId)) {
            throw new SecurityException("Bu hesaba erişim yetkiniz yok!");
        }

        account.setAccountStatus(AccountStatus.CLOSED);
        accountRepository.saveAndFlush(account);

        OutboxEvent outboxEvent = new OutboxEvent();
        outboxEvent.setTopic("account-closed");
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

    
    public List<Account> getAccounts(String token){

        UUID userId = jwtUtil.extractUserId(token);
        if (!userFlagsRepository.existsByUserIdAndProfileCompleteTrue(userId)) {
            throw new IllegalStateException("Kullanıcı profil bilgileri tamamlanmadan önce mevcut işleme devam edilemez.");
        }

        return accountRepository.findByUserId(userId);
    }

    public Account getAccount(String token,UUID accountId){

        UUID userId = jwtUtil.extractUserId(token);
        if (!userFlagsRepository.existsByUserIdAndProfileCompleteTrue(userId)) {
            throw new IllegalStateException("Kullanıcı profil bilgileri tamamlanmadan önce mevcut işleme devam edilemez.");
        }
        
        Account account = accountRepository.findById(accountId)
                                .orElseThrow(() -> new IllegalArgumentException("Mevcut bir hesap bulunamadi!"));

                
        if (!account.getUserId().equals(userId)) {
            throw new SecurityException("Bu hesaba erişim yetkiniz yok!");
        }
        return account;
    }


    public ResponseBalanceDto getBalance(String token,UUID accountId) {

        UUID userId = jwtUtil.extractUserId(token);
        if (!userFlagsRepository.existsByUserIdAndProfileCompleteTrue(userId)) {
            throw new IllegalStateException("Kullanıcı profil bilgileri tamamlanmadan önce mevcut işleme devam edilemez.");
        }
        
        Account account = accountRepository.findById(accountId)
                                    .orElseThrow(() -> new IllegalArgumentException("Mevcut bir hesap bulunamadi!"));
     
        if (!account.getUserId().equals(userId)) {
            throw new SecurityException("Bu hesaba erişim yetkiniz yok!");
        }

        return new ResponseBalanceDto(account.getBalance(), account.getAvailableBalance());
    }

    @Transactional
    public void proccessLedgerEvent(Map<String,Object> data){
        try {
            //Gelen tüm zorunlu değerleri alıp null kontrolü uyguluyoruz
            String accountIdStr = (String) data.get("accountId");
            String transactionIdStr = (String) data.get("transactionId");
            String transactionTypeStr = (String) data.get("transactionType");
            String currencyStr = (String) data.get("currency");
            Object amountObj = data.get("amount");
            String targetIdStr = (String) data.get("targetId"); 
            
            if (accountIdStr == null || transactionIdStr == null || transactionTypeStr == null || currencyStr == null || amountObj == null) {
                throw new IllegalArgumentException("Ledger Event'te zorunlu alanlar eksik.");
            }

            UUID accountId = UUID.fromString(accountIdStr);
            UUID transactionId = UUID.fromString(transactionIdStr);
            TransactionType transactionType = TransactionType.valueOf(transactionTypeStr);
            Currency currency = Currency.valueOf(currencyStr);
            BigDecimal amount = new BigDecimal(amountObj.toString());
            
            // targetId ve alıcı kontrolü sadece Transfer için gerekli
            UUID targetId = null;
            
            if (transactionType == TransactionType.TRANSFER) {
                
                if (targetIdStr == null) {
                    throw new IllegalArgumentException(transactionTypeStr + " işlemi için hedef hesap (targetId) eksik.");
                }
                
                targetId = UUID.fromString(targetIdStr);
                
                // Alıcı kullanıcı 
                UUID receiverId = accountRepository.findById(targetId)
                                    .orElseThrow(() -> new IllegalArgumentException("Hedef hesap bulunamadı!"))
                                    .getUserId();

                if (!userFlagsRepository.existsByUserIdAndProfileCompleteTrue(receiverId)) {
                    throw new IllegalStateException("Alıcı kullanıcı profil bilgileri tamamlanmadan önce mevcut işleme devam edilemez.");
                }
            }
            

            UUID senderId = accountRepository.findById(accountId)
                                .orElseThrow(() -> new IllegalArgumentException("Gönderici hesap bulunamadı!"))
                                .getUserId();
            
            if (!userFlagsRepository.existsByUserIdAndProfileCompleteTrue(senderId)) {
                throw new IllegalStateException("Gönderici kullanıcı profil bilgileri tamamlanmadan önce mevcut işleme devam edilemez.");
            }


            switch(transactionType){
                case TRANSFER -> {
                    decreaseBalance(transactionId, accountId, currency, amount); // Gönderen
                    increaseBalance(transactionId, targetId, currency, amount);  // Alan
                }
                case DEPOSIT -> increaseBalance(transactionId, accountId, currency, amount);
                case WITHDRAW -> decreaseBalance(transactionId, accountId, currency, amount); 
            }

            // TODO : Notification gibi servisler için burada outbox eventini oluştur

        } catch (Exception e) {
            Object transactionId = data != null ? data.get("transactionId") : "Bilinmiyor";
            log.error("Ledger event işlenirken hata oluştu! Transaction id: {}, Hata: {}", transactionId, e.getMessage(), e);
        }
        

        
    }

    @Transactional
    public void increaseBalance(UUID transactionId, UUID accountId, Currency currency, BigDecimal amount){

        Account account = accountRepository.findById(accountId)
                                    .orElseThrow(() -> new IllegalArgumentException("Mevcut bir hesap bulunamadi!"));


        account.setBalance(account.getBalance().add(amount));
        account.setUpdatedAt(Instant.now());
        accountRepository.save(account);

                            

    }

    @Transactional
    public void decreaseBalance(UUID transactionId, UUID targetId, Currency currency, BigDecimal amount){
        Account account = accountRepository.findById(targetId)
                                    .orElseThrow(() -> new IllegalArgumentException("Mevcut bir hesap bulunamadi!"));

        account.setBalance(account.getBalance().subtract(amount));
        account.setUpdatedAt(Instant.now());
        accountRepository.save(account);
                            
    }

      private OutboxEvent buildOutboxEvent(UUID transactionId, TransactionType type, Map<String, Object> payload) {
        try {
            return OutboxEvent.builder()
                  //  .id(transactionId) 
                    .topic("topic?." + type.name().toLowerCase()) // TODO : Notification için topic adı bak
                    .payload(objectMapper.writeValueAsString(payload))
                    .build();
        } catch (JsonProcessingException e) {
            log.error("Outbox serileştirirken hata meydana geldi: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public ResponseAccountInfoDto getAccountInfo(UUID accountId){

        Account account = accountRepository.findById(accountId)
                                    .orElseThrow(() -> new IllegalArgumentException("Mevcut bir hesap bulunamadi!"));


        UUID userId=account.getUserId();

        if (!userFlagsRepository.existsByUserIdAndProfileCompleteTrue(userId)) {
            throw new IllegalStateException("Kullanıcı profil bilgileri tamamlanmadan önce mevcut işleme devam edilemez.");
        }
        
        return new ResponseAccountInfoDto(account.getCurrency(),account.getBalance());
    }
    
    
}
