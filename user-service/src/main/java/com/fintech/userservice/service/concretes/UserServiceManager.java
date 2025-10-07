package com.fintech.userservice.service.concretes;

import com.fintech.userservice.dto.request.CompleteProfileRequestDto;
import com.fintech.userservice.dto.response.CompleteProfileResponseDto;
import com.fintech.userservice.event.AuthEvent;
import com.fintech.userservice.event.EventData;
import com.fintech.userservice.event.UserChangedEmailData;
import com.fintech.userservice.event.UserCreatedData;
import com.fintech.userservice.event.UserDeletedData;
import com.fintech.userservice.exception.ProfileAlreadyCompleted;
import com.fintech.userservice.model.ProfileStatus;
import com.fintech.userservice.model.User;
import com.fintech.userservice.repository.UserRepository;
import com.fintech.userservice.service.abstracts.UserService;

import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;

@Service
public class UserServiceManager implements UserService {

    private final UserRepository userRepository;
    private static final Logger log = LoggerFactory.getLogger(UserServiceManager.class);
    

    public UserServiceManager(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    public User createUserFromEvent(AuthEvent<EventData> authEvent) {
        
        if ((authEvent.getData() instanceof UserCreatedData)) {
            UserCreatedData eventData = (UserCreatedData)authEvent.getData();
           
            if (userRepository.existsById(eventData.getUserId())) {
                log.info("{} idli kullanıcı zaten mevcut.", eventData.getUserId());
                return userRepository.findById(eventData.getUserId().toString()).orElse(null);

            } 
            
            UUID userId = UUID.fromString(eventData.getUserId());
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


    public CompleteProfileResponseDto completeProfile(UUID id,CompleteProfileRequestDto request){

        User user = userRepository.findByIdAndDeletedAtIsNull(id)
                    .orElseThrow(() -> new IllegalArgumentException("Kullanici bulunamadi"));

        if(user.getProfileStatus() == ProfileStatus.COMPLETE){
            throw new ProfileAlreadyCompleted("Profil bilgileri zaten önceden doldurulmuş"); 
        }

        user.setFirstName(request.firstName());
        user.setLastName(request.lastName());
        user.setNationalId(request.nationalId());
        user.setPhoneNumber(request.phoneNumber());
        user.setProfileStatus(ProfileStatus.COMPLETE);
   
        userRepository.save(user);

        return new CompleteProfileResponseDto(
            user.getFirstName(), 
            user.getLastName(), 
            user.getPhoneNumber(), 
            user.getNationalId(), 
            user.getProfileStatus(), 
            user.getUpdatedAt());
    }

   
    public User changeEmailFromEvent(AuthEvent<EventData> authEvent) {

        if(authEvent.getData() instanceof UserChangedEmailData) {

            UserChangedEmailData eventData = (UserChangedEmailData) authEvent.getData();
            
            User user = userRepository.findByIdAndDeletedAtIsNull(UUID.fromString(eventData.getUserId()))
                        .orElseThrow(() -> new IllegalArgumentException("Kullanici bulunamadi"));

            user.setEmail(eventData.getNewEmail());
            user.setTokenVersion(eventData.getTokenVersion());
            userRepository.save(user);
            log.info("Kullanıcı emaili güncellendi: {}", user);
            return user;
        }

        log.warn("Beklenmeyen event veri tipi: {}", authEvent.getData().getClass().getName());
        return null;

    }


    public User deleteUserFromEvent(AuthEvent<EventData> authEvent) {

        if(authEvent.getData() instanceof UserDeletedData) {
            UserDeletedData eventData = (UserDeletedData) authEvent.getData();

            User user = userRepository.findByIdAndDeletedAtIsNull(UUID.fromString(eventData.getUserId()))
                        .orElseThrow(() -> new IllegalArgumentException("Kullanici bulunamadi"));

            user.softDelete();
            user.setTokenVersion(eventData.getTokenVersion());

            userRepository.save(user);
            log.info("Kullanıcı silindi: {}", user);
            return user;
        }

        log.warn("Beklenmeyen event veri tipi: {}", authEvent.getData().getClass().getName());
        return null;
    }
    
}
