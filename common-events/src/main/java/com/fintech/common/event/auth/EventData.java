package com.fintech.common.event.auth;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.EXTERNAL_PROPERTY,
    property = "eventType" // Bu özellik adı, AuthEvent sınıfındaki alan adıyla eşleşmeli
)
@JsonSubTypes({
    @JsonSubTypes.Type(value = UserCreatedData.class, name = "USER_CREATED"),
    @JsonSubTypes.Type(value = PasswordChangedData.class, name = "PASSWORD_CHANGED"),
    @JsonSubTypes.Type(value = UserDeletedData.class, name = "USER_DELETED"),
    @JsonSubTypes.Type(value = UserChangedEmailData.class, name = "EMAIL_CHANGED")
})
public interface EventData {
    
}
