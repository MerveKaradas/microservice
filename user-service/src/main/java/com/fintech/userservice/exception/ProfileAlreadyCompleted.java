package com.fintech.userservice.exception;

public class ProfileAlreadyCompleted extends RuntimeException {

    String message = "Profil bilgileri zaten doldurulmuş";

    public ProfileAlreadyCompleted(String message) {
        super(message);
    }
    
}
