package com.fintech.authservice.service.concretes;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fintech.authservice.dto.request.UserRegisterRequestDto;
import com.fintech.authservice.dto.request.UserUpdateEmailRequestDto;
import com.fintech.authservice.dto.request.UserUpdatePasswordRequestDto;
import com.fintech.authservice.dto.response.UserResponseDto;
import com.fintech.authservice.dto.response.UserTokenStatusResponseDto;
import com.fintech.authservice.exception.AuthenticationException;
import com.fintech.authservice.exception.GlobalExceptionHandler;
import com.fintech.authservice.exception.UserAlreadyExistsException;
import com.fintech.authservice.mapper.UserMapper;
import com.fintech.authservice.model.User;
import com.fintech.authservice.repository.UserRepository;
import com.fintech.authservice.security.JwtUtil;
import com.fintech.authservice.service.abstarcts.AuthService;
import com.fintech.common.event.auth.AuthEvent;
import com.fintech.common.event.auth.AuthEventType;
import com.fintech.common.event.auth.PasswordChangedData;
import com.fintech.common.event.auth.UserChangedEmailData;
import com.fintech.common.event.auth.UserCreatedData;
import com.fintech.common.event.auth.UserDeletedData;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;

import java.util.Map;
import java.util.UUID;

@Service
public class AuthServiceManager implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final JwtBlacklistService jwtBlacklistService;
    private final StringRedisTemplate redisTemplate; 
    private long refreshTokenTtl;
    private final ApplicationEventPublisher eventPublisher;
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @Value("${spring.application.name}")
    private String serviceName;


    public AuthServiceManager(UserRepository userRepository,
                                PasswordEncoder passwordEncoder, 
                                JwtUtil jwtUtil,
                                JwtBlacklistService jwtBlacklistService, 
                                StringRedisTemplate redisTemplate, 
                                @Value("${jwt.refreshExpiration}")long refreshTokenTtl,
                                ApplicationEventPublisher eventPublisher) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.jwtBlacklistService = jwtBlacklistService;
        this.redisTemplate = redisTemplate;
        this.refreshTokenTtl = refreshTokenTtl;
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    public UserResponseDto registerUser(UserRegisterRequestDto requestDto) {

        // Email kontrolü
        if (userRepository.existsByEmail(requestDto.getEmail())) {
            throw new UserAlreadyExistsException("Bu email zaten kullanılıyor.");
        }

        // Parola şifreleme
        if (requestDto.getPassword() == null || requestDto.getPassword().isEmpty()) {
            throw new IllegalArgumentException("Parola boş olamaz.");
        }

        String hashedPassword = passwordEncoder.encode(requestDto.getPassword());

        User user = UserMapper.toEntity(requestDto, hashedPassword);

        userRepository.save(user);

        AuthEvent<UserCreatedData> event = AuthEvent.<UserCreatedData>builder()
            .eventId(UUID.randomUUID().toString())
            .eventType(AuthEventType.USER_CREATED)
            .timestamp(java.time.Instant.now())
            .source(serviceName)
            .data(new UserCreatedData(
                user.getId().toString(), user.getEmail(), user.getRole(), user.getCreatedAt(), user.getTokenVersion()))
            .build();    

        eventPublisher.publishEvent(event);

        return UserMapper.toDto(user);
    }


    public Map<String,String> login(String email, String password,HttpServletResponse response) {

        User user = userRepository.findByEmailAndDeletedAtIsNull(email)
                .orElseThrow(() -> new AuthenticationException("Geçersiz kullanıcı adı veya şifre"));

        if (!passwordEncoder.matches(password, user.getPasswordHash())) {
            throw new AuthenticationException("Geçersiz kullanıcı adı veya şifre");
        }

        Map<String, String> tokens = new HashMap<>();
        String refreshToken = jwtUtil.generateRefreshToken(user);

        tokens.put("accessToken", jwtUtil.generateAccessToken(user));
        tokens.put("refreshToken", refreshToken);

        long ttlSeconds = refreshTokenTtl / 1000; //Redis ve cookie için saniye cinsinden yazıyoruz
        redisTemplate.opsForValue().set("refresh:" + jwtUtil.getJtiFromToken(refreshToken) , user.getId().toString(), ttlSeconds, TimeUnit.SECONDS);

        // Refresh token’ı HTTP-only cookie olarak ayarlıyoruz
        Cookie cookie = new Cookie("refreshToken", refreshToken);
        cookie.setHttpOnly(true); // JS erişemez
        cookie.setSecure(false); // TODO : https ise true değilse false, ancak dev ortamında false yapılabilir hata çıkmaması adına 
        cookie.setPath("/");  // site genelinde geçerli
        cookie.setMaxAge((int) ttlSeconds); // saniye cinsinden geçerlilik
        cookie.setDomain("localhost");  

        response.addCookie(cookie);  

        return tokens;
        
    }

    public void logout(HttpServletRequest request,HttpServletResponse response) {
       
        String accessToken = jwtUtil.resolveToken(request);
     
        if (accessToken != null) {
            try {
                if (jwtUtil.validateToken(accessToken)) {
                    String jti = jwtUtil.getJtiFromToken(accessToken);
                    long remainingTtl = jwtUtil.getRemainingTtlSeconds(accessToken);
            
                    jwtBlacklistService.blacklist(jti, remainingTtl);
                }
            } catch (Exception e) {
                logger.error("Access token geçersiz(expired)", e);
            }
        }
        String refreshToken = jwtUtil.resolveRefreshTokenFromCookie(request);
       

        if (refreshToken != null) {
            try {
                if (jwtUtil.validateToken(refreshToken)) {
                    String refreshJti = jwtUtil.getJtiFromToken(refreshToken);
                    
                    redisTemplate.delete("refresh:" + refreshJti);
                }
            } catch (Exception e) {
                logger.error("Refresh token geçersiz(expired)", e);
            }

            Cookie cookie = new Cookie("refreshToken", null);
            cookie.setHttpOnly(true);
            cookie.setSecure(true);
            cookie.setPath("/");
            cookie.setMaxAge(0);
            response.addCookie(cookie);
            logger.info("Refresh token cookie silindi");
        }

    }


    // Refresh token ile yeni token alma
    public Map<String, String> refresh(String oldRefreshToken, HttpServletResponse response) {

        if (!jwtUtil.validateToken(oldRefreshToken)) {
            throw new RuntimeException("Refresh token geçersiz!");
        }

        String oldJti = jwtUtil.getJtiFromToken(oldRefreshToken);

        // Redisde refresh token geçerliliği kontrolü
        String userIdStr = redisTemplate.opsForValue().get("refresh:" + oldJti);
        if (userIdStr == null) {
            throw new RuntimeException("Refresh token süresi dolmuş veya iptal edilmiş!");
        }

        UUID userId = UUID.fromString(userIdStr); 
        User user = userRepository.findByIdAndDeletedAtIsNull(userId)
            .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı!"));

        // Yeni access ve refresh tokenlar
        String newAccessToken = jwtUtil.generateAccessToken(user);
        String newRefreshToken = jwtUtil.generateRefreshToken(user);

        // Redis’e yeni refresh token ekleme
        String newJti = jwtUtil.getJtiFromToken(newRefreshToken);
        long ttlSeconds = refreshTokenTtl / 1000; //Redis ve cookie için saniye cinsinden yazıyoruz

        redisTemplate.opsForValue().set("refresh:" + newJti, user.getId().toString(),
                ttlSeconds, TimeUnit.SECONDS);
        redisTemplate.delete("refresh:" + oldJti);

        Map<String, String> tokens = new HashMap<>();
        tokens.put("accessToken", newAccessToken);
        tokens.put("refreshToken", newRefreshToken);

        // Yeni token sonrası Cookie güncelleme
        Cookie cookie = new Cookie("refreshToken", tokens.get("refreshToken"));
        cookie.setHttpOnly(true);
        cookie.setPath("/");          // site genelinde geçerli
        cookie.setMaxAge((int) ttlSeconds); // saniye 
        cookie.setSecure(true);       
        cookie.setDomain("localhost"); 

        response.addCookie(cookie);

        return tokens;
    }


    @Transactional
    public void updatePassword(UUID userId, UserUpdatePasswordRequestDto requestDto) {

       User user = userRepository.findByIdAndDeletedAtIsNull(userId)
            .orElseThrow(() -> new IllegalArgumentException("Kullanıcı bulunamadı."));

        // Eski şifre kontrolü
        if (!passwordEncoder.matches(requestDto.getOldPassword(), user.getPassword())) {
            throw new BadCredentialsException("Eski parola hatalı!");
        }

        user.setPasswordHash(passwordEncoder.encode(requestDto.getNewPassword()));
        user.setTokenVersion(user.getTokenVersion() + 1); // Tüm tokenları geçersiz kılmak için 

        userRepository.save(user);

        AuthEvent<PasswordChangedData> event = AuthEvent.<PasswordChangedData>builder()
            .eventId(UUID.randomUUID().toString())
            .eventType(AuthEventType.PASSWORD_CHANGED)
            .timestamp(java.time.Instant.now())
            .source(serviceName)
            .data(new PasswordChangedData(
                user.getId().toString(),
                user.getEmail(),
                user.getTokenVersion()
            ))
            .build();    

        eventPublisher.publishEvent(event);


    }


    @Transactional
    public void deleteUser(String currentAccessToken) {
        
        // Kimliği doğrulanmış kullanıcı
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof User)) {
            throw new IllegalStateException("Kullanıcı kimliği doğrulanmamış.");
        }

        User user = (User) authentication.getPrincipal();

        user.softDelete();

        // Token versiyonu artırılıyor mevcut tokenların geçersiz kılınması için
        user.setTokenVersion(user.getTokenVersion() + 1);

        userRepository.save(user);

        //  mevcut access token blacklist’e ekleniyor
        if (currentAccessToken != null && jwtUtil.validateToken(currentAccessToken)) {
            String jti = jwtUtil.getJtiFromToken(currentAccessToken);
            long ttl = jwtUtil.getRemainingTtlSeconds(currentAccessToken);
            jwtBlacklistService.blacklist(jti, ttl);
        }

        AuthEvent<UserDeletedData> event = AuthEvent.<UserDeletedData>builder()
            .eventId(UUID.randomUUID().toString())
            .eventType(AuthEventType.USER_DELETED)
            .timestamp(java.time.Instant.now())
            .source(serviceName)
            .data(new UserDeletedData(
                user.getId().toString(),
                user.getEmail(),
                user.getDeletedAt(),
                user.getTokenVersion()
            ))
            .build();    

        eventPublisher.publishEvent(event);


    }


    @Transactional
    public void updateEmail(UUID userId, UserUpdateEmailRequestDto requestDto) {

       User user = userRepository.findByIdAndDeletedAtIsNull(userId)
            .orElseThrow(() -> new IllegalArgumentException("Kullanıcı bulunamadı."));

        // Request email unique kontrolü
          if (requestDto.getNewEmail() != null &&
            !requestDto.getNewEmail().isEmpty() &&
            !user.getEmail().equals(requestDto.getNewEmail()) &&
            userRepository.existsByEmail(requestDto.getNewEmail())) {
            throw new UserAlreadyExistsException("Bu email zaten kullanılıyor.");
        }

        String oldEmail = user.getEmail();

        user.setEmail(requestDto.getNewEmail());
        user.setTokenVersion(user.getTokenVersion() + 1); // Tüm tokenları geçersiz kılmak için

        userRepository.save(user);

        AuthEvent<UserChangedEmailData> event = AuthEvent.<UserChangedEmailData>builder()
            .eventId(UUID.randomUUID().toString())
            .eventType(AuthEventType.EMAIL_CHANGED)
            .timestamp(java.time.Instant.now())
            .source(serviceName)
            .data(new UserChangedEmailData(
                user.getId().toString(),
                oldEmail,
                user.getEmail(),
                user.getTokenVersion()

            ))
            .build();    

        eventPublisher.publishEvent(event);
    }


    public Integer getTokenVersion(UUID userId) {
        
        User user = userRepository.findByIdAndDeletedAtIsNull(userId)
            .orElseThrow(() -> new IllegalArgumentException("Kullanıcı bulunamadı."));

        return user.getTokenVersion();
    }

    public UserTokenStatusResponseDto getTokenStatus(UUID userId) {
        User user = userRepository.findByIdAndDeletedAtIsNull(userId)
            .orElseThrow(() -> new IllegalArgumentException("Kullanıcı bulunamadı."));

        if (user.getDeletedAt() != null) {
            throw new IllegalArgumentException("Kullanıcı silinmiş.");
        }

        boolean isDeleted = user.getDeletedAt() != null; // eğer deletedAt null değilse kullanıcı silinmiş
  
        return new UserTokenStatusResponseDto(user.getTokenVersion(), isDeleted);
    }
    
}
