package com.fintech.authservice.service.concretes;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.fintech.authservice.event.AuthEvent;
import com.fintech.authservice.event.EventData;

@Service
public class EventPublisher {
    
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public EventPublisher(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    } 

    public void publish(AuthEvent event) {
        kafkaTemplate.send("auth-events", event.getEventId(), event);
    }
    
}
