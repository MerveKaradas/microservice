package com.fintech.userservice.exception;

public class UserAlreadyExistsException extends RuntimeException {

    String message = "Bu kullanıcı zaten mevcut.";
    
    public UserAlreadyExistsException(String message) {
        super(message);
    }


    
}
