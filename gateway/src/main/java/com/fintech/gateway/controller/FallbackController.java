package com.fintech.gateway.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

@RestController
public class FallbackController {


    //TODO : Gelişmiş fallback mekanizması oluşturulacak 
    @RequestMapping("/fallback/auth")
    public ResponseEntity<String> authServiceFallback() {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
              .body("Auth Service şuan kullanılamıyor. Lütfen daha sonra tekrar deneyiniz.");
    }
    
}
