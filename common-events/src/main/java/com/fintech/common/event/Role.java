package com.fintech.common.event;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum Role {
    @JsonProperty("USER")
    USER,
    @JsonProperty("ADMIN")
    ADMIN
    
}
