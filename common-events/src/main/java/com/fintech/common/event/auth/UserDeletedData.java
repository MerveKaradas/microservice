package com.fintech.common.event.auth;

import java.time.Instant;

public class UserDeletedData implements EventData  {
    
    String userId;
    String email;
    Integer tokenVersion;
    Instant deletedAt;

    
    public UserDeletedData() { //Jackson için boş constructor
    }

    public UserDeletedData(String userId, String email, Instant deletedAt,Integer tokenVersion) {
        this.userId = userId;
        this.email = email;
        this.deletedAt = deletedAt;
        this.tokenVersion = tokenVersion;
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

    public Integer getTokenVersion() {
        return tokenVersion;
    }

    public void setTokenVersion(Integer tokenVersion) {
        this.tokenVersion = tokenVersion;
    }

    
    
    
}
