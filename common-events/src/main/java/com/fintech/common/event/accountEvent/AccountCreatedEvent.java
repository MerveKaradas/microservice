package com.fintech.common.event.accountEvent;

public class AccountCreatedEvent {

    String accountId;
    String userId;

    public AccountCreatedEvent(String accountId, String userId) {
        this.accountId = accountId;
        this.userId = userId;
    }
    
    public String getAccountId() {
        return accountId;
    }
    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }
    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }

    
}
