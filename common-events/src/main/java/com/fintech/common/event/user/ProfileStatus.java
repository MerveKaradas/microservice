package com.fintech.common.event.user;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum ProfileStatus {
    @JsonProperty("INCOMPLETE")
    INCOMPLETE,
    @JsonProperty("COMPLETE")
    COMPLETE
}
