package com.fintech.authservice.dto.response;

import com.fintech.authservice.model.Role;
import java.io.Serializable;
import java.util.UUID;

public class UserResponseDto implements Serializable{

    private static final long serialVersionUID = 2001L;

    private UUID id;
    private String email;
    private Role role;

    public UserResponseDto(UUID id, String email, Role role) {
        this.id = id;
        this.email = email;
        this.role = role;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    
}
