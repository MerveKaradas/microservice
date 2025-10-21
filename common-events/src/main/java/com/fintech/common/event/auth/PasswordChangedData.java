package com.fintech.common.event.auth;

public class PasswordChangedData implements EventData  {
    
    String userId;
    String email;
    Integer tokenVersion;

    public PasswordChangedData() {
    }

    public PasswordChangedData(String userId, String email, Integer tokenVersion) {
        this.userId = userId;
        this.email = email;
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

    public Integer getTokenVersion() {
        return tokenVersion;
    }

    public void setTokenVersion(Integer tokenVersion) {
        this.tokenVersion = tokenVersion;
    }
}
