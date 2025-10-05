package com.fintech.authservice.controller;

import java.util.Map;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fintech.authservice.dto.request.UserLoginRequestDto;
import com.fintech.authservice.dto.request.UserRegisterRequestDto;
import com.fintech.authservice.dto.request.UserUpdateEmailRequestDto;
import com.fintech.authservice.dto.request.UserUpdatePasswordRequestDto;
import com.fintech.authservice.dto.response.UserResponseDto;
import com.fintech.authservice.model.User;
import com.fintech.authservice.service.abstarcts.AuthService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
   
    public AuthController(AuthService authService) {
        this.authService = authService;
       
    }

    @PostMapping("/signUp")
    public ResponseEntity<UserResponseDto> register(
            @RequestBody @Valid UserRegisterRequestDto requestDto) {

        return ResponseEntity.ok(authService.registerUser(requestDto));

    }

    @PostMapping("/login")
    public ResponseEntity<Map<String,String>> login(
            @RequestBody @Valid UserLoginRequestDto requestDto,HttpServletResponse response) {
               
        Map<String,String> tokens = authService.login(requestDto.getEmail(), requestDto.getPassword(),response);
        return ResponseEntity.ok(tokens);
    
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request, 
                                        HttpServletResponse response) { 
        authService.logout(request, response);
        return ResponseEntity.ok("Başarıyla çıkış yapıldı");
    }

    // INFO : Refresh token geçerli olduğu sürece, yeni access ve refresh token üretir (frontend kullanımında)
    @PostMapping("/refresh") 
    public ResponseEntity<Map<String,String>> refresh(@CookieValue("refreshToken") String refreshToken, //refresh token cookie'de, access token headerda
                                                      HttpServletResponse response) {
        Map<String, String> newTokens = authService.refresh(refreshToken, response);
        return ResponseEntity.ok(newTokens);
    }

    @PatchMapping("/me/password")
    public ResponseEntity<String> updateUserPassword(
            @Valid @RequestBody UserUpdatePasswordRequestDto requestDto,
            @AuthenticationPrincipal UserDetails userDetails) {

        UUID userId = ((User) userDetails).getId();
        authService.updatePassword(userId, requestDto);

        return ResponseEntity.ok("Password başarılı bir şekilde güncellendi"); 
    }

    @PatchMapping("/me/email")
    public ResponseEntity<String> updateUserEmail(
            @Valid @RequestBody UserUpdateEmailRequestDto requestDto,
            @AuthenticationPrincipal UserDetails userDetails) {

        UUID userId = ((User) userDetails).getId();
        authService.updateEmail(userId, requestDto);
        return ResponseEntity.ok("Email başarılı bir şekilde güncellendi");
    }


    @DeleteMapping("/me/delete")
    public ResponseEntity<String> deleteCurrentUser(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7);
        authService.deleteUser(token);
        return ResponseEntity.ok("Kullanıcı başarılı bir şekilde silindi");
    }

    // TODO : Bu endpoint için gelişmiş şifre sıfırlama mekanizması oluşturulacak
    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword() {
        return ResponseEntity.ok("Şifre sıfırlama talimatları e-posta adresinize gönderildi.");
    }

    


    

    
    
}
