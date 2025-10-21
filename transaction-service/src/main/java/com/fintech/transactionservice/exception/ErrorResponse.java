package com.fintech.transactionservice.exception;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ErrorResponse {

    private int status;
    private String message;
    private String error;
    private String path;
    private Instant timestamp;
    
  
    
}
