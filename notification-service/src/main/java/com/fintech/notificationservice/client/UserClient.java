package com.fintech.notificationservice.client;

import java.util.UUID;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.fintech.common.event.user.ResponseUserInfoDto;

@FeignClient(name="user-service", path="/api/users")
public interface UserClient {

    @GetMapping("/getInfo/{userId}")
    ResponseEntity<ResponseUserInfoDto> getUserInfo(@PathVariable("userId") UUID userId);
    


    
}
