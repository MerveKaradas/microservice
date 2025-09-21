package com.fintech.authservice.service.concretes;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.fintech.authservice.dto.request.UserRequestDto;
import com.fintech.authservice.dto.response.UserResponseDto;
import com.fintech.authservice.exception.AuthenticationException;
import com.fintech.authservice.exception.PhoneNumberAlreadyExistsException;
import com.fintech.authservice.exception.UserAlreadyExistsException;
import com.fintech.authservice.mapper.UserMapper;
import com.fintech.authservice.model.User;
import com.fintech.authservice.repository.UserRepository;
import com.fintech.authservice.security.JwtUtil;
import com.fintech.authservice.service.abstarcts.AuthService;

import org.springframework.beans.factory.annotation.Value;

import java.util.Map;

@Service
public class AuthServiceManager implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final JwtBlacklistService jwtBlacklistService;
    private final StringRedisTemplate redisTemplate; 
    @Value("${jwt.refreshExpiration}") 
    private long refreshTokenTtl;
    

    public AuthServiceManager(UserRepository userRepository,PasswordEncoder passwordEncoder, JwtUtil jwtUtil,JwtBlacklistService jwtBlacklistService, StringRedisTemplate redisTemplate, long refreshTokenTtl) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.jwtBlacklistService = jwtBlacklistService;
        this.redisTemplate = redisTemplate;
        this.refreshTokenTtl = refreshTokenTtl;
    }

    public UserResponseDto registerUser(UserRequestDto requestDto) {

        // Email kontrolü
        if (userRepository.existsByEmail(requestDto.getEmail())) {
            throw new UserAlreadyExistsException("Bu email zaten kullanılıyor.");
        }

        // Telefon numara kontrolü
        if (userRepository.existsByPhoneNumber(requestDto.getPhoneNumber())) {
            throw new PhoneNumberAlreadyExistsException("Bu telefon no kullanılıyor.");
        }

        // Parola şifreleme
        if (requestDto.getPassword() == null || requestDto.getPassword().isEmpty()) {
            throw new IllegalArgumentException("Parola boş olamaz.");
        }

        String hashedPassword = passwordEncoder.encode(requestDto.getPassword());

        User user = UserMapper.toEntity(requestDto, hashedPassword);

        userRepository.save(user);
     
        return UserMapper.toDto(user);
    }


     public Map<String,String> login(String email, String password) {

        User user = userRepository.findByEmailAndDeletedAtIsNull(email)
                .orElseThrow(() -> new AuthenticationException("Geçersiz kullanıcı adı veya şifre"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new AuthenticationException("Geçersiz kullanıcı adı veya şifre");
        }

        Map<String, String> tokens = new HashMap<>();
        tokens.put("accessToken", jwtUtil.generateAccessToken(user));
        tokens.put("refreshToken", jwtUtil.generateRefreshToken(user));

        return tokens;
        
    }

    public void logout(String authHeader, String refreshToken) {

        String token = authHeader.replace("Bearer ", "");
        String jti = jwtUtil.getJtiFromToken(token);
        long ttlSeconds = jwtUtil.getRemainingTtlSeconds(token);
        
        jwtBlacklistService.blacklist(jti, ttlSeconds);


         // Access token blacklist
        String accessToken = authHeader.replace("Bearer ", "");
        String accessJti = jwtUtil.getJtiFromToken(accessToken);
        jwtBlacklistService.blacklist(accessJti, jwtUtil.getRemainingTtlSeconds(accessToken));

        // Refresh token Redis’den sil
        String refreshJti = jwtUtil.getJtiFromToken(refreshToken);
        redisTemplate.delete("refresh:" + refreshJti);

    }

    // Refresh token ile yeni token alma
    public Map<String, String> refresh(String oldRefreshToken) {

        if (!jwtUtil.validateToken(oldRefreshToken)) {
            throw new RuntimeException("Refresh token geçersiz!");
        }

        String oldJti = jwtUtil.getJtiFromToken(oldRefreshToken);

        // Redis veya DB’de refresh token geçerliliğini kontrol et
        String userIdStr = redisTemplate.opsForValue().get("refresh:" + oldJti);
        if (userIdStr == null) {
            throw new RuntimeException("Refresh token süresi dolmuş veya iptal edilmiş!");
        }

        User user = userRepository.findByIdAndDeletedAtIsNull(Long.parseLong(userIdStr))
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı!"));

        // Yeni access ve refresh token üret
        String newAccessToken = jwtUtil.generateAccessToken(user);
        String newRefreshToken = jwtUtil.generateRefreshToken(user);

        // Redis’e yeni refresh token kaydet ve eskiyi sil
        String newJti = jwtUtil.getJtiFromToken(newRefreshToken);

        
        redisTemplate.opsForValue().set("refresh:" + newJti, user.getId().toString(),
                refreshTokenTtl, TimeUnit.SECONDS);
        redisTemplate.delete("refresh:" + oldJti);

        Map<String, String> tokens = new HashMap<>();
        tokens.put("accessToken", newAccessToken);
        tokens.put("refreshToken", newRefreshToken);
        return tokens;
    }

    
}
