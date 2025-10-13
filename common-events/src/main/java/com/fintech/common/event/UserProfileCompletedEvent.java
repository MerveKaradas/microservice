package com.fintech.common.event;

import java.io.Serializable;

import com.fintech.common.event.ProfileStatus;

public class UserProfileCompletedEvent implements Serializable {

    private static final long serialVersionUID = 1L;
    private String userId;
    private ProfileStatus profileStatus;
    
    public UserProfileCompletedEvent() {
    }

    public UserProfileCompletedEvent(String userId, ProfileStatus profileStatus) {
        this.userId = userId;
        this.profileStatus = profileStatus;
    }

    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    public ProfileStatus getProfileStatus() {
        return profileStatus;
    }
    public void setProfileStatus(ProfileStatus profileStatus) {
        this.profileStatus = profileStatus;
    }

    
}
