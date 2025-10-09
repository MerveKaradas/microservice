package com.fintech.accountservice.controller;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fintech.accountservice.service.abstracts.AccountService;
import com.fintech.accountservice.dto.response.*;
import com.fintech.accountservice.model.Account;
import com.fintech.accountservice.dto.request.*;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping("/createAccount")
    public ResponseEntity<Account> createAccount(
        @RequestHeader("Authorization") String token,
        @Valid RequestCreateAccountDto requestCreateAccountDto) {
        return ResponseEntity.ok(accountService.createAccount(token, requestCreateAccountDto));
    }



    
}
