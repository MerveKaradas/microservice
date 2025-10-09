package com.fintech.accountservice.event;


import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.fintech.accountservice.service.abstracts.AccountService;

@Component
public class UserEventConsumer {
    
    private final AccountService accountService;
    private static final Logger logger = LoggerFactory.getLogger(UserEventConsumer.class);

    public UserEventConsumer(AccountService accountService) {
        this.accountService = accountService;
    }


    @KafkaListener(topics = "user-profileCompleted", groupId = "account-service")
    public void consume(UserProfileCompletedEvent event) {
    
        switch (event.profileStatus()) {
            case COMPLETED : 
                accountService.createDefaultAccount(UUID.fromString(event.userId()));
                break;
            case INCOMPLETE:
                logger.info("Kullanıcı profili tamamlanmamış : " + event.userId());
                break;
            
            default:
                logger.warn("Bilinmeyen profil durum türü: " + event.profileStatus());
                break;
                        
        }
       
    }

    

    
    
}
