package com.fintech.gateway.dto;

import java.io.Serializable;

public class UserTokenStatusResponseDto implements Serializable{

    private final long serialVersionUUID = 2002L;

    private Integer tokenVersion;
    private boolean deleted;

    public UserTokenStatusResponseDto(Integer tokenVersion, boolean deleted) {
        this.tokenVersion = tokenVersion;
        this.deleted = deleted;
    }

    public Integer getTokenVersion() {
        return tokenVersion;
    }

    public void setTokenVersion(Integer tokenVersion) {
        this.tokenVersion = tokenVersion;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
    
}
