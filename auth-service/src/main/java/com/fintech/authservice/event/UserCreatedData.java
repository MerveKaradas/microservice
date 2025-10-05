package com.fintech.authservice.event;

import java.time.Instant;

import com.fintech.authservice.model.Role;

public class UserCreatedData implements EventData {

    String userId;
    String email;
    Role role;
    Instant createdAt;

    public UserCreatedData(String userId, String email, Role role, Instant createdAt) {
        this.userId = userId;
        this.email = email;
        this.role = role;
        this.createdAt = createdAt;
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
    public Role getRole() {
        return role;
    }
    public void setRole(Role role) {
        this.role = role;
    }
    public Instant getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    
} 