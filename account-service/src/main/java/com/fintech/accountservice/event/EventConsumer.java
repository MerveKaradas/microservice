package com.fintech.accountservice.event;


import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutorService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fintech.accountservice.model.base.Account;
import com.fintech.accountservice.model.entity.OutboxEvent;
import com.fintech.accountservice.model.entity.UserFlag;
import com.fintech.accountservice.repository.OutboxRepository;
import com.fintech.accountservice.repository.UserFlagsRepository;
import com.fintech.accountservice.service.abstracts.AccountService;
import com.fintech.common.event.account.AccountCreatedEvent;
import com.fintech.common.event.user.UserProfileCompletedEvent;

@Component
public class EventConsumer {
    
    private final AccountService accountService;
    private final ExecutorService executorService;
    private final OutboxRepository outboxRepository;
    private final UserFlagsRepository userFlagsRepository;
    private final ObjectMapper objectMapper;
    private static final Logger logger = LoggerFactory.getLogger(EventConsumer.class);

    public EventConsumer(AccountService accountService,
                            OutboxRepository outboxRepository,
                            ExecutorService executorService, 
                            ObjectMapper objectMapper, UserFlagsRepository userFlagsRepository) {
        this.accountService = accountService;
        this.outboxRepository = outboxRepository;
        this.executorService = executorService;
        this.userFlagsRepository = userFlagsRepository;
        this.objectMapper = objectMapper;
    }


    @KafkaListener(topics = "user-profileCompleted", groupId = "account-service")
    public void consume(UserProfileCompletedEvent event) throws JsonProcessingException {

        if(event.getProfileStatus().name().equals("COMPLETE")){
            executorService.submit(() -> { // consume thread bloklanmaması için async executor kullandık

                UUID userId = UUID.fromString(event.getUserId());

                UserFlag userFlag = new UserFlag();
                userFlag.setUserId(userId);
                userFlagsRepository.save(userFlag);
                logger.info("Yeni kullanıcı için UserFlag kaydı oluşturuldu: {}", userId);

                userFlagsRepository.markProfileComplete(UUID.fromString(event.getUserId()));
                logger.info("Profil tamamlandı! flag güncellendi: {}  ", event.getUserId());


                try {
                    Account account = accountService.createDefaultAccount(UUID.fromString(event.getUserId()));

                    OutboxEvent outboxEvent = new OutboxEvent();
                    outboxEvent.setTopic("account-created");

                    outboxEvent.setPayload(
                        objectMapper.writeValueAsString(
                            new AccountCreatedEvent(account.getId().toString(), account.getUserId().toString()))
                    );

                    outboxRepository.save(outboxEvent);

                } catch (Exception e) {
                    logger.error("Event işleme sırasında hata: {}", e);
                }
            });
                
        } else {
            logger.info("Profil tamamlanmış userId : {}", event.getUserId());
        }
      

    }



    @KafkaListener(topics= {"ledger.transfer", "ledger.withdraw", "ledger.deposit"}, groupId = "account-service")
    public void handleLedgerEvents(String payload){
        
        try {
            Map<String,Object> data = objectMapper.readValue(payload, new TypeReference<>() {});
            accountService.proccessLedgerEvent(data);

        }catch(Exception ex) {
            logger.error("Ledger event işenirken hata meydana geldi : ", ex);
        }

    }
    
}
