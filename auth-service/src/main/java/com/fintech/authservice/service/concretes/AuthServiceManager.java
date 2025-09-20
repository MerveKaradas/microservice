package com.fintech.authservice.service.concretes;

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

@Service
public class AuthServiceManager implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final JwtBlacklistService jwtBlacklistService;

    public AuthServiceManager(UserRepository userRepository,PasswordEncoder passwordEncoder, JwtUtil jwtUtil,JwtBlacklistService jwtBlacklistService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.jwtBlacklistService = jwtBlacklistService;
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


     public String login(String email, String password) {

        User user = userRepository.findByEmailAndDeletedAtIsNull(email)
                .orElseThrow(() -> new AuthenticationException("Geçersiz kullanıcı adı veya şifre"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new AuthenticationException("Geçersiz kullanıcı adı veya şifre");
        }

        return jwtUtil.generateToken(user);
        
    }

    public void logout(String authHeader) {

        String token = authHeader.replace("Bearer ", "");
        String jti = jwtUtil.getJtiFromToken(token);
        long ttlSeconds = jwtUtil.getRemainingTtlSeconds(token);
        
        jwtBlacklistService.blacklist(jti, ttlSeconds);

    }
    
}
