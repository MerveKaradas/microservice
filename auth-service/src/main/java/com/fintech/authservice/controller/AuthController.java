package com.fintech.authservice.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fintech.authservice.dto.request.UserLoginRequestDto;
import com.fintech.authservice.dto.request.UserRequestDto;
import com.fintech.authservice.dto.response.UserResponseDto;
import com.fintech.authservice.service.abstarcts.AuthService;

import jakarta.servlet.http.HttpServletResponse;
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
    public ResponseEntity<Map<String,String>> login(
            @RequestBody @Valid UserLoginRequestDto requestDto,HttpServletResponse response) {
               
        Map<String,String> tokens = authService.login(requestDto.getEmail(), requestDto.getPassword(),response);
        return ResponseEntity.ok(tokens);
    
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestHeader("Authorization") String authHeader, // acces token headerda 
                                        @CookieValue("refreshToken") String refreshToken, // refresh token cookie'de
                                        HttpServletResponse response) { 
        authService.logout(authHeader, refreshToken,response);
        return ResponseEntity.ok("Başarıyla çıkış yapıldı");
    }


    // INFO : Refresh token geçerli olduğu sürece, yeni access ve refresh token üretir (frontend kullanımında)
    @PostMapping("/refresh") 
    public ResponseEntity<Map<String,String>> refresh(@CookieValue("refreshToken") String refreshToken,
                                                      HttpServletResponse response) {
        Map<String, String> newTokens = authService.refresh(refreshToken, response);
        return ResponseEntity.ok(newTokens);
    }



    // TODO : Bu endpoint için gelişmiş şifre sıfırlama mekanizması oluşturulacak
    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword() {
        return ResponseEntity.ok("Şifre sıfırlama talimatları e-posta adresinize gönderildi.");
    }

    


    

    
    
}
