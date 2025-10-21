package com.fintech.transactionservice.service.concretes;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fintech.common.event.account.dto.response.ResponseAccountInfoDto;
import com.fintech.common.event.account.enums.Currency;
import com.fintech.common.event.transaction.enums.TransactionType;
import com.fintech.transactionservice.client.AccountServiceClient;
import com.fintech.transactionservice.dto.request.RequestTransactionDto;
import com.fintech.transactionservice.exception.AccountServiceException;
import com.fintech.transactionservice.exception.CurrencyMismatchException;
import com.fintech.transactionservice.model.entity.OutboxEvent;
import com.fintech.transactionservice.model.entity.Transaction;
import com.fintech.transactionservice.model.enums.TransactionStatus;
import com.fintech.transactionservice.repository.OutboxRepository;
import com.fintech.transactionservice.repository.TransactionRepository;
import com.fintech.transactionservice.service.abtracts.TransactionService;

@Service
public class TransactionServiceManager implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final OutboxRepository outboxRepository;
    private final AccountServiceClient accountServiceClient;
    private final ObjectMapper objectMapper;
    private static Logger log = LoggerFactory.getLogger(TransactionServiceManager.class);
    
    public TransactionServiceManager(TransactionRepository transactionRepository,
                                    ObjectMapper objectMapper, 
                                    OutboxRepository outboxRepository, AccountServiceClient accountServiceClient) {
        this.transactionRepository = transactionRepository;
        this.outboxRepository = outboxRepository;
        this.accountServiceClient = accountServiceClient;
        this.objectMapper = objectMapper;
    }

    
    @Transactional
    public UUID proccessTransaction(RequestTransactionDto request){

        Transaction transaction = Transaction.builder()
                      .transactionType(TransactionType.valueOf(request.getTransactionType()))
                      .transactionStatus(TransactionStatus.PENDING)
                      .currency(Currency.valueOf(request.getCurrency()))
                      .amount(request.getAmount())
                      .build();
        transactionRepository.save(transaction);

        try {

            ResponseAccountInfoDto accountInfo;
            try {
                accountInfo = accountServiceClient.getAccountInfo(request.getAccountId()).getBody();
            } catch (Exception e) {
                throw new AccountServiceException("Hesap bilgisi alınamadı! " + e);
            }


            if(accountInfo.getCurrency() != Currency.valueOf(request.getCurrency())){
                throw new CurrencyMismatchException("Hesap para birimi uyuşmuyor!");
            }

            if(TransactionType.valueOf(request.getTransactionType()) == TransactionType.TRANSFER){
                
                ResponseAccountInfoDto targetInfo;
                try {
                    targetInfo = accountServiceClient.getAccountInfo(request.getTargetId()).getBody();
                } catch (Exception e) {
                    throw new AccountServiceException("Hedef hesap bilgisi alınamadı! "+  e);
                }

                if (targetInfo.getCurrency() != Currency.valueOf(request.getCurrency())) {
                    throw new CurrencyMismatchException("Hedef hesap para birimi farklı!");
                }

            }

            if(TransactionType.valueOf(request.getTransactionType()) == TransactionType.TRANSFER || TransactionType.valueOf(request.getTransactionType()) == TransactionType.WITHDRAW) {
                if(accountInfo.getBalance().compareTo(request.getAmount()) < 0){
                   throw new IllegalArgumentException("Bakiye yetersiz!");
                }
            }

            transaction.setTransactionStatus(TransactionStatus.COMPLETED);

            Map<String, Object> payload = Map.of(
            "transactionId", transaction.getId().toString(),
            "accountId", request.getAccountId().toString(),
            "targetId", request.getTargetId() != null ? request.getTargetId().toString() : null,
            "transactionType" , transaction.getTransactionType().name(),
            "currency", transaction.getCurrency().name(),
            "amount", transaction.getAmount()
            );


            OutboxEvent outbox = OutboxEvent.builder()
                            .id(transaction.getId())
                            .topic("transaction." + request.getTransactionType().toLowerCase())
                            .build();

            try {
                outbox.setPayload(objectMapper.writeValueAsString(payload));
            }catch(JsonProcessingException ex) {
                log.warn("Hata : {}", ex.getMessage());
                ex.printStackTrace();
            }
            

            outboxRepository.save(outbox);
            

        } catch(Exception ex) {

            log.error("Transaction gönderimi başarısız! Hata : ",ex);
            transaction.setTransactionStatus(TransactionStatus.FAILED);
            transaction.setFailedReason(ex.getMessage());
            transactionRepository.save(transaction);

            throw ex;

        } finally {
            transaction.setUpdateAt();
        }

        return transaction.getId();
    }

    
}
