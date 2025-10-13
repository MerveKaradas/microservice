package com.fintech.userservice.service.concretes;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import com.fintech.common.event.UserProfileCompletedEvent;

@Component
public class EventListener {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    EventListener(KafkaTemplate<String, Object> kafkaTemplate){
        this.kafkaTemplate = kafkaTemplate;
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void publishUserProfileCompleted(UserProfileCompletedEvent event) {
        kafkaTemplate.send("user-profileCompleted", event.getUserId(),event);
    }


}
