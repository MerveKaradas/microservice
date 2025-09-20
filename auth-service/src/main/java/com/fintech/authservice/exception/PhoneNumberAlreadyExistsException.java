package com.fintech.authservice.exception;

public class PhoneNumberAlreadyExistsException extends RuntimeException {
    String message;
    
    public PhoneNumberAlreadyExistsException(String message) {
        super(message);
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
