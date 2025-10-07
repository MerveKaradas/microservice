package com.fintech.userservice.event;

import com.fintech.userservice.model.ProfileStatus;

public record UserProfileCompletedEvent(
    String userId,
    ProfileStatus profileStatus
       

) {}
