package com.fintech.common.event;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum ProfileStatus {
    @JsonProperty("INCOMPLETE")
    INCOMPLETE,
    @JsonProperty("COMPLETE")
    COMPLETE
}
