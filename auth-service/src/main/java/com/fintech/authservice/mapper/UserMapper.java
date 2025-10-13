package com.fintech.authservice.mapper;

import com.fintech.authservice.dto.request.UserRegisterRequestDto;
import com.fintech.authservice.dto.response.UserResponseDto;
import com.fintech.common.event.Role;
import com.fintech.authservice.model.User;

public class UserMapper {

    public static User toEntity(UserRegisterRequestDto dto, String encodedPassword) {
        if (dto == null) {
            return null;
        }

        return new User(
            dto.getEmail(),
            encodedPassword,
            Role.USER
        );
    }

    public static UserResponseDto toDto(User entity) {
        if (entity == null) {
            return null;
        }
        return new UserResponseDto(
            entity.getId(),
            entity.getEmail(),
            entity.getRole()
        );
    }
    
}
