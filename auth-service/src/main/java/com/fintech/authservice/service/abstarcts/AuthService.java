package com.fintech.authservice.service.abstarcts;

import java.util.Map;
import java.util.UUID;

import com.fintech.authservice.dto.request.UserRegisterRequestDto;
import com.fintech.authservice.dto.request.UserUpdateEmailRequestDto;
import com.fintech.authservice.dto.request.UserUpdatePasswordRequestDto;
import com.fintech.authservice.dto.response.UserResponseDto;
import com.fintech.authservice.dto.response.UserTokenStatusResponseDto;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public interface AuthService {

    UserResponseDto registerUser(UserRegisterRequestDto requestDto);

    Map<String,String> login(String email, String password,HttpServletResponse response);

    void logout(HttpServletRequest request,HttpServletResponse response);

    Map<String, String> refresh(String oldRefreshToken, HttpServletResponse response);

    void updatePassword(UUID userId, UserUpdatePasswordRequestDto requestDto);

    void deleteUser(String token);

    void updateEmail(UUID userId, UserUpdateEmailRequestDto requestDto);

    Integer getTokenVersion(UUID userId);

    UserTokenStatusResponseDto getTokenStatus(UUID userId);
    
}
