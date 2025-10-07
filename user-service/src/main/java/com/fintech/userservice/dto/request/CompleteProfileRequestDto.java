package com.fintech.userservice.dto.request;

public record CompleteProfileRequestDto(
    String firstName,
    String lastName,  
    String phoneNumber,
    String nationalId
) {}
