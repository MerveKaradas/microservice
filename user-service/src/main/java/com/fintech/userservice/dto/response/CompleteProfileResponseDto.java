package com.fintech.userservice.dto.response;

import java.time.Instant;

import com.fintech.common.event.user.ProfileStatus;

public record CompleteProfileResponseDto(
    String firstName,
    String lastName,
    String phoneNumber,
    String nationalId,  
    ProfileStatus profileStatus,
    Instant updatedAt
) {}
