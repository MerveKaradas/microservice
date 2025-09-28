package com.fintech.userservice.service.abstracts;

import java.util.Optional;

import com.fintech.userservice.event.UserRegisteredEvent;
import com.fintech.userservice.model.User;

public interface UserService {

    Optional<User> createUserFromEvent(UserRegisteredEvent userRegisteredEvent);
    
}
