package com.fintech.transactionservice.client;

import java.util.UUID;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.fintech.common.event.account.dto.response.ResponseAccountInfoDto;

@FeignClient(name = "account-service", path="/api/accounts")
public interface AccountServiceClient {
    
    @GetMapping("/internal/{accountId}")
    ResponseEntity<ResponseAccountInfoDto> getAccountInfo(@PathVariable("accountId") UUID accountId);
    
}
