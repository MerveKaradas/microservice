package com.fintech.userservice.dto.request;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
public class RequestTelephoneNumberDto implements Serializable {

    private final static long serialVersionUID = 1001L;

    @NotBlank(message = "Yeni telefon numarası boş olamaz.")
    @Pattern(
    regexp = "^(?:\\+90|0)?5\\d{9}$",
    message = "Geçerli bir Türk cep telefonu numarası giriniz (örn: 05XXXXXXXXX veya +905XXXXXXXXX)")
    private String newPhoneNumber;

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public String getNewPhoneNumber() {
        return newPhoneNumber;
    }

    public void setNewPhoneNumber(String newPhoneNumber) {
        this.newPhoneNumber = newPhoneNumber;
    }

    
    
}
