package com.fintech.transactionservice.exception;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    public ErrorResponse buildError(HttpStatus httpStatus, String message, HttpServletRequest request){
        return new ErrorResponse().builder()
                    .status(httpStatus.value())
                    .error(httpStatus.getReasonPhrase())
                    .message(message)
                    .path(request.getRequestURI())
                    .timestamp(Instant.now())
                    .build();
    }


    //Methodlar için
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationException(MethodArgumentNotValidException ex) {
        Map<String, Object> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error -> {
            errors.put(error.getField(),error.getDefaultMessage());
        });

        Map<String, Object> body = Map.of(
            "status", 400,
            "error", " Bad request",
            "messages", errors,
            "timestamp", Instant.now()
        ); 

        return ResponseEntity.badRequest().body(body);
    }

    @ExceptionHandler(AccountServiceException.class)
    public ResponseEntity<?> handleAccountException(AccountServiceException ex,HttpServletRequest request){
        return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(buildError(HttpStatus.BAD_GATEWAY,ex.getMessage(),request));
    }
    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneral(Exception ex,
                                                        HttpServletRequest request){
                                                            
        System.out.println("Hata : " +  ex.getMessage());
        log.error("Beklenmeyen bir hata oluştu : {} ",ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(buildError(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), request));
    }

    
}
