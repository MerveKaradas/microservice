package com.fintech.userservice.service.abstracts;

import java.util.UUID;

import com.fintech.common.event.auth.AuthEvent;
import com.fintech.common.event.auth.EventData;
import com.fintech.common.event.user.ResponseUserInfoDto;
import com.fintech.userservice.dto.request.CompleteProfileRequestDto;
import com.fintech.userservice.dto.request.RequestTelephoneNumberDto;
import com.fintech.userservice.dto.response.CompleteProfileResponseDto;
import com.fintech.userservice.model.User;

public interface UserService {

    User createUserFromEvent(AuthEvent<EventData> authEvent);
    User changeEmailFromEvent(AuthEvent<EventData> authEvent);
    User deleteUserFromEvent(AuthEvent<EventData> authEvent);
    
    
    CompleteProfileResponseDto completeProfile(UUID id,CompleteProfileRequestDto request);
    void changeTelephoneNumber(String token, RequestTelephoneNumberDto newPhoneNumber);
    ResponseUserInfoDto getUserInfo(UUID userId);
}
