package com.fintech.userservice.event;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import com.fintech.userservice.service.abstracts.UserService;

@Component
public class UserEventConsumer {
    
    private final UserService userService;

    public UserEventConsumer(UserService userService) {
        this.userService = userService;
    }

    @KafkaListener(topics = "user-registered", groupId = "user-service")
    public void consume(UserRegisteredEvent userRegisteredEvent) {
        userService.createUserFromEvent(userRegisteredEvent);
    }

    
    
}
