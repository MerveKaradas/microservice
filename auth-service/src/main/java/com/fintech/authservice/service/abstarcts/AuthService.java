package com.fintech.authservice.service.abstarcts;

import java.util.Map;

import com.fintech.authservice.dto.request.UserRequestDto;
import com.fintech.authservice.dto.response.UserResponseDto;

import jakarta.servlet.http.HttpServletResponse;



public interface AuthService {

    UserResponseDto registerUser(UserRequestDto requestDto);

    Map<String,String> login(String email, String password,HttpServletResponse response);

    void logout(String authHeader, String refreshToken,HttpServletResponse response);

    Map<String, String> refresh(String oldRefreshToken, HttpServletResponse response);
    
}
