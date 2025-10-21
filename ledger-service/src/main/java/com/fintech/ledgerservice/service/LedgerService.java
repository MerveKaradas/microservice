package com.fintech.ledgerservice.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fintech.common.event.account.enums.Currency;
import com.fintech.common.event.transaction.enums.TransactionType;
import com.fintech.ledgerservice.model.LedgerEntry;
import com.fintech.ledgerservice.model.OutboxEvent;
import com.fintech.ledgerservice.repository.LedgerRepository;
import com.fintech.ledgerservice.repository.OutboxRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.transaction.Transactional;

@Service
public class LedgerService {

    private final LedgerRepository ledgerRepository;
    private final OutboxRepository outboxRepository;
    private final ObjectMapper objectMapper;
    private static Logger log = LoggerFactory.getLogger(LedgerService.class);

    public LedgerService(LedgerRepository ledgerRepository, 
                        OutboxRepository outboxRepository, 
                        ObjectMapper objectMapper) {
        this.ledgerRepository = ledgerRepository;
        this.outboxRepository = outboxRepository;
        this.objectMapper = objectMapper;
    }

    @KafkaListener(topics = {"transaction.transfer", "transaction.deposit", "transaction.withdraw"}, groupId = "ledger-service")
    @Transactional
    public void handleTransactionEvents(String payload) {

        try {
            Map<String, Object> data = objectMapper.readValue(payload, new TypeReference<>() {});
            TransactionType type = TransactionType.valueOf((String) data.get("transactionType"));

            switch (type) {
                case TRANSFER -> handleTransfer(data);
                case DEPOSIT -> handleDeposit(data);
                case WITHDRAW -> handleWithdraw(data);
            }

        } catch(Exception ex) {
            log.error("Transaction yönlendirme sırasında hata meydana geldi ! Hata : {}",ex);

        }
    }

   
    @Transactional
    private void handleTransfer(Map<String, Object> data){

        try {

            UUID transactionId = UUID.fromString((String) data.get("transactionId"));
            UUID accountId = UUID.fromString((String) data.get("accountId"));
            UUID targetId = UUID.fromString((String) data.get("targetId"));
            TransactionType transactionType = TransactionType.valueOf((String) data.get("transactionType")); // Stringden enuma cast için valueOf kullanılır
            Currency currency = Currency.valueOf((String) data.get("currency"));
            BigDecimal amount = new BigDecimal(data.get("amount").toString());


            ledgerRepository.saveAll(List.of(
                LedgerEntry.builder()
                .transactionId(transactionId)
                .accountId(accountId)
                .currency(currency)
                .debit(amount)
                .build() ,

                LedgerEntry.builder()
                .transactionId(transactionId)
                .accountId(targetId)
                .currency(currency)
                .credit(amount)
                .build()
            ));

            outboxRepository.save(buildOutboxEvent(transactionId, transactionType, data));

        }catch(Exception ex) {
            log.error("Transfer işlenirken hata meydana geldi! TransactionId: {}, Hata: {}", data.get("transactionId").toString(), ex.getMessage(), ex);

        }

    }

    @Transactional
    private void handleDeposit(Map<String, Object> data){

        try {

            UUID transactionId = UUID.fromString((String) data.get("transactionId"));
            UUID accountId = UUID.fromString((String) data.get("accountId"));
            UUID targetId = UUID.fromString((String) data.get("targetId"));
            TransactionType transactionType = TransactionType.valueOf((String) data.get("transactionType")); // Stringden enuma cast için valueOf kullanılır
            Currency currency = Currency.valueOf((String) data.get("currency"));
            BigDecimal amount = new BigDecimal(data.get("amount").toString());


            ledgerRepository.save(
                LedgerEntry.builder()
                .transactionId(transactionId)
                .accountId(accountId)
                .currency(currency)
                .credit(amount)
                .build() 
            );

            outboxRepository.save(buildOutboxEvent(transactionId, transactionType, data));

        }catch(Exception ex) {

            log.error("Transfer işlenirken hata meydana geldi! :",ex);

        }

    }

   
    @Transactional
    private void handleWithdraw(Map<String, Object> data){

        try {
           
            UUID transactionId = UUID.fromString((String) data.get("transactionId"));
            UUID accountId = UUID.fromString((String) data.get("accountId"));
            UUID targetId = UUID.fromString((String) data.get("targetId"));
            TransactionType transactionType = TransactionType.valueOf((String) data.get("transactionType")); // Stringden enuma cast için valueOf kullanılır
            Currency currency = Currency.valueOf((String) data.get("currency"));
            BigDecimal amount = new BigDecimal(data.get("amount").toString());


            ledgerRepository.save(
                LedgerEntry.builder()
                .transactionId(transactionId)
                .accountId(accountId)
                .currency(currency) 
                .debit(amount)
                .build() 
            );

            outboxRepository.save(buildOutboxEvent(transactionId, transactionType, data));


        }catch(Exception ex) {

            log.error("Transfer işlenirken hata meydana geldi! :",ex);

        }

    }

  

    private OutboxEvent buildOutboxEvent(UUID transactionId, TransactionType type, Map<String, Object> payload) {
        try {
            return OutboxEvent.builder()
                 //   .id(transactionId)
                    .topic("ledger." + type.name().toLowerCase())
                    .payload(objectMapper.writeValueAsString(payload))
                    .build();
        } catch (JsonProcessingException e) {
            log.error("Outbox serileştirirken hata meydana geldi: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    
    
    
}
