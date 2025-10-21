package com.fintech.common.event.auth;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum Role {
    @JsonProperty("USER")
    USER,
    @JsonProperty("ADMIN")
    ADMIN
    
}
