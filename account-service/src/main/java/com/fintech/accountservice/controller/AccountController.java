package com.fintech.accountservice.controller;

import java.util.List;
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

import com.fintech.accountservice.service.abstracts.AccountService;
import com.fintech.accountservice.dto.request.*;
import com.fintech.accountservice.dto.response.ResponseBalanceDto;
import com.fintech.accountservice.model.base.Account;

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
        @Valid @RequestBody RequestCreateAccountDto requestCreateAccountDto) {
        return ResponseEntity.ok(accountService.createAccount(token, requestCreateAccountDto));
    }

    @GetMapping
    public ResponseEntity<List<Account>> getAccounts(@RequestHeader("Authorization") String token){
        return ResponseEntity.ok(accountService.getAccounts(token));
    }

    @GetMapping("/{accountId}")
    public ResponseEntity<Account> getAccount(@PathVariable("accountId") UUID accountId,
                                              @RequestHeader("Authorization") String token){
        return ResponseEntity.ok(accountService.getAccount(token,accountId));
    }

    @GetMapping("/{accountId}/balance")
    public ResponseEntity<ResponseBalanceDto> getBalance(@PathVariable("accountId") UUID accountId,
                                            @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(accountService.getBalance(token, accountId));
    }

    @PostMapping("/{accountId}/close")
    public ResponseEntity<Account> closeAccount(@PathVariable("accountId") UUID accountId,
                                                @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(accountService.closeAccount(token, accountId));
    }




    
}
