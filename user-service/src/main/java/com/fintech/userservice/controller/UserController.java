package com.fintech.userservice.controller;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fintech.userservice.service.abstracts.UserService;
import com.fintech.userservice.dto.request.CompleteProfileRequestDto;
import com.fintech.userservice.dto.response.CompleteProfileResponseDto;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/{id}/completeProfile")
    public ResponseEntity<CompleteProfileResponseDto> completeProfile(@PathVariable UUID id,
                                            @RequestBody CompleteProfileRequestDto request){
        return ResponseEntity.ok(userService.completeProfile(id,request));
    }
    
    
}
