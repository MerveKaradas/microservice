package com.fintech.authservice.controller;

import java.util.Collections;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fintech.authservice.dto.request.UserLoginRequestDto;
import com.fintech.authservice.dto.request.UserRequestDto;
import com.fintech.authservice.dto.response.UserResponseDto;
import com.fintech.authservice.service.abstarcts.AuthService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
   
    public AuthController(AuthService authService) {
        this.authService = authService;
       
    }

    @PostMapping("/signUp")
    public ResponseEntity<UserResponseDto> register(
            @RequestBody @Valid UserRequestDto requestDto) {

        return ResponseEntity.ok(authService.registerUser(requestDto));

    }

    @PostMapping("/login")
    public ResponseEntity<?> login(
            @RequestBody @Valid UserLoginRequestDto requestDto) {
               
        String token = authService.login(requestDto.getEmail(), requestDto.getPassword());
        return ResponseEntity.ok(Collections.singletonMap("token", token));
    
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestHeader("Authorization") String authHeader) {
        authService.logout(authHeader);
        return ResponseEntity.ok("Başarıyla çıkış yapıldı");
    }

    


    

    
    
}
