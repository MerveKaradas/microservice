package com.fintech.authservice.event;

import java.time.Instant;

import com.fintech.authservice.model.Role;

public record UserRegisteredEvent(
    String userId,
    String email,
    Role role,
    Instant createdAt 
) {}
