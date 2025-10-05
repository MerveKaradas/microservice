package com.fintech.authservice.event;

import java.time.Instant;

public class UserDeletedData implements EventData  {
    
    String userId;
    String email;
    Instant deletedAt;

    
    public UserDeletedData(String userId, String email, Instant deletedAt) {
        this.userId = userId;
        this.email = email;
        this.deletedAt = deletedAt;
    }
    
    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public Instant getDeletedAt() {
        return deletedAt;
    }
    public void setDeletedAt(Instant deletedAt) {
        this.deletedAt = deletedAt;
    }

    
    
}
