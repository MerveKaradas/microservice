package com.fintech.authservice.service.abstarcts;

import com.fintech.authservice.dto.request.UserRequestDto;
import com.fintech.authservice.dto.response.UserResponseDto;



public interface AuthService {

    UserResponseDto registerUser(UserRequestDto requestDto);

    String login(String email, String password);

    void logout(String authHeader);
    
}
