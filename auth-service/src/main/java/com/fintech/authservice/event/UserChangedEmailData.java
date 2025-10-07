package com.fintech.authservice.event;

public class UserChangedEmailData implements EventData {
    
    String userId;
    String oldEmail;
    String newEmail;
    Integer tokenVersion;

    public UserChangedEmailData(String userId, String oldEmail, String newEmail,Integer tokenVersion) {
        this.userId = userId;
        this.oldEmail = oldEmail;
        this.newEmail = newEmail;
        this.tokenVersion = tokenVersion;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getOldEmail() {
        return oldEmail;
    }

    public void setOldEmail(String oldEmail) {
        this.oldEmail = oldEmail;
    }

    public String getNewEmail() {
        return newEmail;
    }

    public void setNewEmail(String newEmail) {
        this.newEmail = newEmail;
    }

    public Integer getTokenVersion() {
        return tokenVersion;
    }
    
    
}
