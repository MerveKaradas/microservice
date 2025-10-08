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

    @KafkaListener(topics = "auth-events", groupId = "user-service")
    public void consume(AuthEvent<EventData> authEvent) {
    
        switch (authEvent.getEventType()) {
            case USER_CREATED : 
                userService.createUserFromEvent(authEvent);
                break;
            case EMAIL_CHANGED:
                userService.changeEmailFromEvent(authEvent);
                break;
            case USER_DELETED:
                userService.deleteUserFromEvent(authEvent);
                break;
            default:
                System.out.println("Bilinmeyen event türü: " + authEvent.getEventType());
                break;
                        
        }
       
    }

    
    
}
