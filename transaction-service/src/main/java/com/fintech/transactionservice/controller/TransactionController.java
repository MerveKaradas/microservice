package com.fintech.transactionservice.controller;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.fintech.transactionservice.dto.request.RequestTransactionDto;
import com.fintech.transactionservice.service.abtracts.TransactionService;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping("/transaction")
    public ResponseEntity<String> transfer(@RequestBody @Valid RequestTransactionDto request){
        transactionService.proccessTransaction(request);
        return ResponseEntity.ok("Transaction işlemi başarılı bir şekilde gerçekleşti!");
    }



    
}
