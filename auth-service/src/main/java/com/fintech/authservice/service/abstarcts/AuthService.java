package com.fintech.authservice.service.abstarcts;

import java.util.Map;

import com.fintech.authservice.dto.request.UserRequestDto;
import com.fintech.authservice.dto.response.UserResponseDto;



public interface AuthService {

    UserResponseDto registerUser(UserRequestDto requestDto);

    Map<String,String> login(String email, String password);

    void logout(String authHeader, String refreshToken);

    Map<String, String> refresh(String oldRefreshToken);
    
}
