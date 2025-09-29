package com.fintech.userservice.dto.response;

import java.time.Instant;

import com.fintech.userservice.model.ProfileStatus;
import com.fintech.userservice.model.Role;

public record CompleteProfileResponseDto(
    String firstName,
    String lastName,
    String email,       
    String phoneNumber,
    String nationalId,  
    Role role,        
    String status,   
    ProfileStatus profileStatus,
    Instant createdAt,
    Instant updatedAt
) {}
