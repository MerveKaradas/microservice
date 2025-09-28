package com.fintech.userservice.event;

public record UserRegisteredEvent(
        String userId,
        String email,
        String fullName
) {}
