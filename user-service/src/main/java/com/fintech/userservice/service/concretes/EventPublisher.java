package com.fintech.userservice.service.concretes;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.fintech.userservice.event.UserProfileCompletedEvent;

@Service
public class EventPublisher {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    EventPublisher(KafkaTemplate<String, Object> kafkaTemplate){
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publishUserProfileCompleted(UserProfileCompletedEvent event) {
        kafkaTemplate.send("user-profileCompleted", event.userId(),event);
    }


}
