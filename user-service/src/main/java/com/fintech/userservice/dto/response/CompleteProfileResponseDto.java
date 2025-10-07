package com.fintech.userservice.dto.response;

import java.time.Instant;

import com.fintech.userservice.model.ProfileStatus;

public record CompleteProfileResponseDto(
    String firstName,
    String lastName,
    String phoneNumber,
    String nationalId,  
    ProfileStatus profileStatus,
    Instant updatedAt
) {}
