package com.fintech.common.event;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum AuthEventType {
    @JsonProperty("USER_CREATED")
    USER_CREATED,
    @JsonProperty("PASSWORD_CHANGED")
    PASSWORD_CHANGED,
    @JsonProperty("EMAIL_CHANGED")
    EMAIL_CHANGED,
    @JsonProperty("USER_DELETED")
    USER_DELETED
}
