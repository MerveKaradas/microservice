package com.fintech.authservice.service.concretes;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.fintech.authservice.event.UserRegisteredEvent;

@Service
public class EventPublisher {
    
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public EventPublisher(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    } 

    public void publishUserRegistered(UserRegisteredEvent event) {
        kafkaTemplate.send("user-registered", event.userId(), event);
    }
    
}
