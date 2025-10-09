package com.fintech.accountservice.event;

enum ProfileStatus {
    COMPLETED,
    INCOMPLETE
}

public record UserProfileCompletedEvent(
    String userId,
    ProfileStatus profileStatus
) {}
