package com.fintech.authservice.event;

public class UserChangedEmail implements EventData {
    
    String userId;
    String oldEmail;
    String newEmail;

    public UserChangedEmail(String userId, String oldEmail, String newEmail) {
        this.userId = userId;
        this.oldEmail = oldEmail;
        this.newEmail = newEmail;
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
    
}
