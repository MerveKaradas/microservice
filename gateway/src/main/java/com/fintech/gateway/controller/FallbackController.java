package com.fintech.gateway.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

@RestController
@RequestMapping("/fallback")
public class FallbackController {

    //TODO : Gelişmiş fallback mekanizması oluşturulacak 
    @RequestMapping("/auth")
    public ResponseEntity<String> authServiceFallback() {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
              .body("Auth Service şuan kullanılamıyor. Lütfen daha sonra tekrar deneyiniz.");
    }

    //TODO : Gelişmiş fallback mekanizması oluşturulacak 
    @RequestMapping("/user")
    public ResponseEntity<String> userServiceFallback() {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
              .body("User Service şuan kullanılamıyor. Lütfen daha sonra tekrar deneyiniz.");
    }

    //TODO : Gelişmiş fallback mekanizması oluşturulacak 
    @RequestMapping("/account")
    public ResponseEntity<String> accountServiceFallback() {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
              .body("Account Service şuan kullanılamıyor. Lütfen daha sonra tekrar deneyiniz.");
    }

    //TODO : Gelişmiş fallback mekanizması oluşturulacak 
    @RequestMapping("/transaction")
    public ResponseEntity<String> transactionServiceFallback() {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
              .body("Transaction Service şuan kullanılamıyor. Lütfen daha sonra tekrar deneyiniz.");
    }


    
}
