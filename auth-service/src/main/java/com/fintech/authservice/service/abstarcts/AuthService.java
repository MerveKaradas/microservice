package com.fintech.authservice.service.abstarcts;

import java.util.Map;

import com.fintech.authservice.dto.request.UserRegisterRequestDto;
import com.fintech.authservice.dto.response.UserResponseDto;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;



public interface AuthService {

    UserResponseDto registerUser(UserRegisterRequestDto requestDto);

    Map<String,String> login(String email, String password,HttpServletResponse response);

    void logout(HttpServletRequest request,HttpServletResponse response);

    Map<String, String> refresh(String oldRefreshToken, HttpServletResponse response);
    
}
