package com.fintech.userservice.service.abstracts;

import java.util.UUID;

import com.fintech.userservice.dto.request.CompleteProfileRequestDto;
import com.fintech.userservice.dto.response.CompleteProfileResponseDto;
import com.fintech.userservice.event.UserRegisteredEvent;
import com.fintech.userservice.model.User;

public interface UserService {

    User createUserFromEvent(UserRegisteredEvent userRegisteredEvent);
    
    CompleteProfileResponseDto completeProfile(UUID id,CompleteProfileRequestDto request);
}
