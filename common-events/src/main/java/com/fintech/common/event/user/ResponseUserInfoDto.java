package com.fintech.common.event.user;

import lombok.Data;

@Data
public class ResponseUserInfoDto {

    String email;
    private String firstName;
    private String lastName;

    public ResponseUserInfoDto() {
    }

    public ResponseUserInfoDto(String email, String firstName, String lastName) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    
    
    
}
