package com.fintech.userservice.exception;

public class ProfileStatusInCompleteException extends RuntimeException {

     String message = "Profil bilgileri tamamlanmadan diğer işlemlere devam edilemez. Lütfen devam etmeden önce profil bilgilerinizi tamamlayınız.";
    

    public ProfileStatusInCompleteException(String message) {
        super(message);
    }
}