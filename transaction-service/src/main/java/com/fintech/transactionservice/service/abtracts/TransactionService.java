package com.fintech.transactionservice.service.abtracts;

import java.util.UUID;

import com.fintech.transactionservice.dto.request.RequestTransactionDto;

public interface TransactionService {

    UUID proccessTransaction(RequestTransactionDto request);
    
}
