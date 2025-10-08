package com.fintech.userservice.dto.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class CompleteProfileRequestDto {

    private final static long serialVersionUID = 1000L;

    @NotBlank(message = "Ad alanı boş geçilemez")
    @Size(min=2, max=50, message = "Ad alanı 2 ile 50 karakter arasında olmalıdır")
    String firstName;

    @NotBlank(message = "Ad alanı boş geçilemez")
    @Size(min=2, max=50, message = "Soyadı alanı 2 ile 75 karakter arasında olmalıdır")
    String lastName;
    
    @Pattern(
    regexp = "^[1-9]\\d{9}[02468]$",
    message = "Geçerli bir T.C. kimlik numarası giriniz (11 haneli, sadece rakam ve sonu çift olmalı)")
    @NotBlank(message = "T.C. kimlik numarası boş olamaz")
    private String nationalId;

    @NotBlank(message = "Telefon numarası boş olamaz")
    @Pattern(
    regexp = "^(?:\\+90|0)?5\\d{9}$",
    message = "Geçerli bir Türk cep telefonu numarası giriniz (örn: 05XXXXXXXXX veya +905XXXXXXXXX)")
    private String phoneNumber;

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getNationalId() {
        return nationalId;
    }

    public void setNationalId(String nationalId) {
        this.nationalId = nationalId;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    


}
