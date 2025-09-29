package com.fintech.userservice.event;

import java.time.Instant;

import com.fintech.userservice.model.Role;

public class UserRegisteredEvent {

   private String userId;
   private String email;
   private Role role;
   private Instant createdAt;

   
   UserRegisteredEvent() {
        
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
