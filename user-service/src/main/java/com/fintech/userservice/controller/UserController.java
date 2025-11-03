package com.fintech.userservice.controller;

import java.util.UUID;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fintech.userservice.service.abstracts.UserService;
import com.fintech.common.event.user.ResponseUserInfoDto;
import com.fintech.userservice.dto.request.CompleteProfileRequestDto;
import com.fintech.userservice.dto.request.RequestTelephoneNumberDto;
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
                                            @Valid @RequestBody CompleteProfileRequestDto request){
        return ResponseEntity.ok(userService.completeProfile(id,request));
    }

    @PostMapping("/me/changeTelephoneNumber")
    public ResponseEntity<String> changeTelephoneNumber(
                            @RequestHeader("Authorization") String token,
                            @RequestBody @Valid RequestTelephoneNumberDto newPhoneNumber){
         userService.changeTelephoneNumber(token,newPhoneNumber);
        return ResponseEntity.ok("Telefon numarası başarılı bir şekilde değiştirildi.");
    }

    @GetMapping("/getInfo/{userId}")
    public ResponseEntity<ResponseUserInfoDto> getUserInfo(@PathVariable("userId") UUID userId){
        return ResponseEntity.ok(userService.getUserInfo(userId));
        
    }


    
    
}
