package com.fintech.authservice.exception;

public class InvalidCredentialsException extends RuntimeException {
    
    String message;
    
    public InvalidCredentialsException(String message) {
        super(message);
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
