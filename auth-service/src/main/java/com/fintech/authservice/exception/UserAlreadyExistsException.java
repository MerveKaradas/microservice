package com.fintech.authservice.exception;


public class UserAlreadyExistsException extends RuntimeException {

    String message = "Bu kullanıcı zaten mevcut.";
    
    public UserAlreadyExistsException(String message) {
        super(message);
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
