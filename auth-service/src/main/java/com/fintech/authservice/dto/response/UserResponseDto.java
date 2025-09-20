package com.fintech.authservice.dto.response;

import com.fintech.authservice.model.Role;
import java.io.Serializable;

public class UserResponseDto implements Serializable{

    private static final long serialVersionUID = 2001L;

    private Long id;
    private String email;
    private String fullName;
    private String phoneNumber;
    private Role role;

    public UserResponseDto(Long id, String email, String fullName, String phoneNumber, Role role) {
        this.id = id;
        this.email = email;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    
}
