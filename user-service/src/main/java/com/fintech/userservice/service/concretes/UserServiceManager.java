package com.fintech.userservice.service.concretes;

import com.fintech.userservice.event.UserRegisteredEvent;
import com.fintech.userservice.model.User;
import com.fintech.userservice.repository.UserRepository;
import com.fintech.userservice.service.abstracts.UserService;

import java.util.Optional;

import org.springframework.stereotype.Service;

@Service
public class UserServiceManager implements UserService {

    private final UserRepository userRepository;

    

     public UserServiceManager(UserRepository userRepository) {
        this.userRepository = userRepository;
    }



    public Optional<User> createUserFromEvent(UserRegisteredEvent userRegisteredEvent) {

        return userRepository.findById(userRegisteredEvent.userId());
    }
    
}
