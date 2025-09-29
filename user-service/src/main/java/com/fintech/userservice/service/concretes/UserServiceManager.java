package com.fintech.userservice.service.concretes;

import com.fintech.userservice.dto.request.CompleteProfileRequestDto;
import com.fintech.userservice.dto.response.CompleteProfileResponseDto;
import com.fintech.userservice.event.UserRegisteredEvent;
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


    public User createUserFromEvent(UserRegisteredEvent userRegisteredEvent) {

        UUID userId = UUID.fromString(userRegisteredEvent.getUserId());  // String → UUID

        if (userRepository.existsById(userId.toString())){
            log.info("{} idli kullanıcı zaten mevcut.", userId);
            return userRepository.findById(userId.toString()).orElse(null);
        }

        User user = User.builder()
            .userId(userId)
            .email(userRegisteredEvent.getEmail())
            .createdAt(userRegisteredEvent.getCreatedAt())
            .role(userRegisteredEvent.getRole())
            .profileStatus(ProfileStatus.INCOMPLETE)
            .build();

        return userRepository.save(user);
    }


    public CompleteProfileResponseDto completeProfile(UUID id,CompleteProfileRequestDto request){

        User user = userRepository.findById(id.toString())
                    .orElseThrow(() -> new IllegalArgumentException("Kullanici bulunamadi"));

        if(user.getProfileStatus() == ProfileStatus.COMPLETE){
            throw new ProfileAlreadyCompleted("Profil bilgileri zaten önceden doldurulmuş"); 
        }

        user.setFirstName(request.firstName());
        user.setLastName(request.lastName());
        user.setNationalId(request.nationalId());
        user.setPhoneNumber(request.phoneNumber());
        user.setStatus(request.status());
        user.setProfileStatus(ProfileStatus.COMPLETE);
   
        userRepository.save(user);

        return new CompleteProfileResponseDto(
            user.getFirstName(), 
            user.getLastName(), 
            user.getEmail(), 
            user.getPhoneNumber(), 
            user.getNationalId(), 
            user.getRole(), 
            user.getStatus(), 
            user.getProfileStatus(), 
            user.getCreatedAt(), 
            user.getUpdatedAt());
    }

    
}
