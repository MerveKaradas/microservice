package com.fintech.accountservice.exception;

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
public class GlobalExceptionHandler{

    private final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    public ErrorResponse buildError(HttpStatus httpStatus, String message, HttpServletRequest request) {
        return new ErrorResponse.Builder()
                    .status(httpStatus.value())
                    .error(httpStatus.getReasonPhrase())
                    .message(message)
                    .path(request.getRequestURI())
                    .timestamp(Instant.now())
                    .build();
    }

    //Metot seviyesinde hata yakalama
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String,Object>> handleValidationExceptions(MethodArgumentNotValidException ex){

        Map<String,Object> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error -> {
            errors.put(error.getField(), error.getDefaultMessage());
        });

          Map<String, Object> body = Map.of(
            "status", 400,
            "error", "Bad Request",
            "messages", errors,
            "timestamp", Instant.now()
        );
        return ResponseEntity.badRequest().body(body);
    }




    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneral(Exception ex,
                                                       HttpServletRequest request) {
        
        System.out.println("Hata: " + ex.getMessage());
        log.error("Beklenmeyen hata oluştu", ex); 
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(buildError(HttpStatus.INTERNAL_SERVER_ERROR, "Beklenmeyen bir hata oluştu", request));
    }

    
}
