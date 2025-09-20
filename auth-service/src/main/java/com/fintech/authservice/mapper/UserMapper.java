package com.fintech.authservice.mapper;

import com.fintech.authservice.dto.request.UserRequestDto;
import com.fintech.authservice.dto.response.UserResponseDto;
import com.fintech.authservice.model.User;

public class UserMapper {

    public static User toEntity(UserRequestDto dto, String encodedPassword) {
        if (dto == null) {
            return null;
        }

        return new User(
            dto.getEmail(),
            dto.getFullName(),
            encodedPassword,
            dto.getPhoneNumber(),
            dto.getRole()
        );
    }

    public static UserResponseDto toDto(User entity) {
        if (entity == null) {
            return null;
        }
        return new UserResponseDto(
            entity.getId(),
            entity.getEmail(),
            entity.getFullName(),
            entity.getPhoneNumber(),
            entity.getRole()
        );
    }
    
}
