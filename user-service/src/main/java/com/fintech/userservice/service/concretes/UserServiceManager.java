package com.fintech.userservice.service.concretes;

import com.fintech.userservice.dto.request.CompleteProfileRequestDto;
import com.fintech.userservice.dto.request.RequestTelephoneNumberDto;
import com.fintech.userservice.dto.response.CompleteProfileResponseDto;
import com.fintech.common.event.auth.AuthEvent;
import com.fintech.common.event.auth.EventData;
import com.fintech.common.event.auth.UserChangedEmailData;
import com.fintech.common.event.auth.UserCreatedData;
import com.fintech.common.event.auth.UserDeletedData;
import com.fintech.common.event.user.ProfileStatus;
import com.fintech.common.event.user.UserProfileCompletedEvent;
import com.fintech.userservice.exception.ProfileAlreadyCompleted;
import com.fintech.userservice.exception.ProfileStatusInCompleteException;
import com.fintech.userservice.model.User;
import com.fintech.userservice.repository.UserRepository;
import com.fintech.userservice.service.abstracts.UserService;
import com.fintech.userservice.security.JwtUtil;

import java.time.Instant;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceManager implements UserService {

    private final UserRepository userRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final JwtUtil jwtUtil;
    private static final Logger log = LoggerFactory.getLogger(UserServiceManager.class);
    

    public UserServiceManager(UserRepository userRepository, ApplicationEventPublisher eventPublisher, JwtUtil jwtUtil ) {
        this.userRepository = userRepository;
        this.eventPublisher = eventPublisher;
        this.jwtUtil = jwtUtil;
    }


    @Transactional
    public User createUserFromEvent(AuthEvent<EventData> authEvent) {
        

        if ((authEvent.getData() instanceof UserCreatedData)) {
            UserCreatedData eventData = (UserCreatedData)authEvent.getData();
           
            UUID userId = UUID.fromString(eventData.getUserId());
            if (userRepository.existsById(userId)) {
                log.info("{} idli kullanıcı zaten mevcut.", eventData.getUserId());
                return userRepository.findById(userId).orElse(null);

            } 
            
            User user = User.builder()
                        .userId(userId)
                        .email(eventData.getEmail())
                        .createdAt(eventData.getCreatedAt())
                        .tokenVersion(eventData.getTokenVersion())
                        .role(eventData.getRole())
                        .profileStatus(ProfileStatus.INCOMPLETE)
                        .build();

            log.info("Yeni kullanıcı oluşturuldu: {}", user);
            return userRepository.save(user);
           
        } 

        log.warn("Beklenmeyen event veri tipi: {}", authEvent.getData().getClass().getName());
        return null;
        

    }


    @Transactional
    public CompleteProfileResponseDto completeProfile(UUID id,CompleteProfileRequestDto request){

        User user = userRepository.findByIdAndDeletedAtIsNull(id)
                    .orElseThrow(() -> new IllegalArgumentException("Kullanici bulunamadi"));

        if(user.getProfileStatus() == ProfileStatus.COMPLETE){
            throw new ProfileAlreadyCompleted("Profil bilgileri zaten önceden doldurulmuş"); 
        }

        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setNationalId(request.getNationalId());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setProfileStatus(ProfileStatus.COMPLETE);
        user.setUpdatedAt(Instant.now());
   
        userRepository.save(user);

        eventPublisher.publishEvent(
            new UserProfileCompletedEvent(user.getId().toString(), user.getProfileStatus()));
       
        return new CompleteProfileResponseDto(
            user.getFirstName(), 
            user.getLastName(), 
            user.getPhoneNumber(), 
            user.getNationalId(), 
            user.getProfileStatus(), 
            user.getUpdatedAt());
    }

   
    @Transactional
    public User changeEmailFromEvent(AuthEvent<EventData> authEvent) {

        if(authEvent.getData() instanceof UserChangedEmailData) {

            UserChangedEmailData eventData = (UserChangedEmailData) authEvent.getData();
            
            User user = userRepository.findByIdAndDeletedAtIsNull(UUID.fromString(eventData.getUserId()))
                        .orElseThrow(() -> new IllegalArgumentException("Kullanici bulunamadi"));

            user.setEmail(eventData.getNewEmail());
            user.setTokenVersion(eventData.getTokenVersion());
            user.setUpdatedAt(Instant.now());
            userRepository.save(user);
            log.info("Kullanıcı emaili güncellendi: {}", user);
            return user;
        }

        log.warn("Beklenmeyen event veri tipi: {}", authEvent.getData().getClass().getName());
        return null;

    }

    @Transactional
    public User deleteUserFromEvent(AuthEvent<EventData> authEvent) {

        if(authEvent.getData() instanceof UserDeletedData) {
            UserDeletedData eventData = (UserDeletedData) authEvent.getData();

            User user = userRepository.findByIdAndDeletedAtIsNull(UUID.fromString(eventData.getUserId()))
                        .orElseThrow(() -> new IllegalArgumentException("Kullanici bulunamadi"));

            user.softDelete();
            user.setTokenVersion(eventData.getTokenVersion());
            user.setUpdatedAt(Instant.now());

            userRepository.save(user);
            log.info("Kullanıcı silindi: {}", user);
            return user;
        }

        log.warn("Beklenmeyen event veri tipi: {}", authEvent.getData().getClass().getName());
        return null;
    }
    
    @Transactional
    public void changeTelephoneNumber(String token, RequestTelephoneNumberDto newPhoneNumber) {

        User user = userRepository.findByIdAndDeletedAtIsNull(UUID.fromString(jwtUtil.extractUsername(token)))
                    .orElseThrow(() -> new IllegalArgumentException("Kullanici bulunamadi"));

        if(user.getProfileStatus() == ProfileStatus.INCOMPLETE) {
            log.error("Profil bilgileri tamamlanmadan telefon numarası değiştirilemez. Lütfen önce profil bilgilerinizi tamamlayınız.");
            throw new ProfileStatusInCompleteException("Profil bilgileri tamamlanmadan diğer işlemlere devam edilemez. Lütfen devam etmeden önce profil bilgilerinizi tamamlayınız.");
        }

        if(userRepository.existsByPhoneNumber(newPhoneNumber.getNewPhoneNumber())) {
            throw new IllegalArgumentException("Bu telefon numarası zaten başka bir kullanıcı tarafından kullanılıyor.");
        } 

        if(user.getPhoneNumber().equals(newPhoneNumber.getNewPhoneNumber())) {
            throw new IllegalArgumentException("Yeni telefon numarası mevcut numara ile aynı olamaz.");
        }
        //TODO : İleride daha gelişmiş doğrulamalar eklenebilir. OTP vs.

        user.setPhoneNumber(newPhoneNumber.getNewPhoneNumber());
        user.setUpdatedAt(Instant.now());
        user.setTokenVersion(user.getTokenVersion() + 1);

        userRepository.save(user);
        log.info("Kullanıcı telefon numarası güncellendi: {}", user);


      
    }
}
