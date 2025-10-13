package com.fintech.accountservice.model;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "user_flags")
public class UserFlag {

    @Id
    @Column(columnDefinition = "uuid")
    private UUID userId;

    @Column(name="profile_complete",nullable = false)
    private boolean profileComplete = false;


    public UUID getUserId() {
         return userId;
    }
    public void setUserId(UUID userId) {
        this.userId = userId; 
    }

    public boolean isProfileComplete() { 
        return profileComplete; 
    }
    public void setProfileComplete(boolean profileComplete) { 
        this.profileComplete = profileComplete; 
    }
    
}
